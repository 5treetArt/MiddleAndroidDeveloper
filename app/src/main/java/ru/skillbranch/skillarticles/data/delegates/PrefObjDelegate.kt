package ru.skillbranch.skillarticles.data.delegates

import androidx.datastore.preferences.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.skillbranch.skillarticles.data.PrefManager
import ru.skillbranch.skillarticles.data.adapters.JsonAdapter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefObjDelegate<T>(
    private val adapter: JsonAdapter<T>,
    private val customKey: String? = null
) {
    operator fun provideDelegate(
        thisRef: PrefManager,
        prop: KProperty<*>
    ): ReadWriteProperty<PrefManager, T?> {

        val key = stringPreferencesKey(customKey ?: prop.name)
        return object : ReadWriteProperty<PrefManager, T?> {
            private var _storedValue: String? = null

            override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
                if (_storedValue == null) {
                    //async flow
                    val flowValue = thisRef.dataStore.data.map { prefs ->
                        prefs[key]
                    }
                    //sync read on IO Dispatchers and return result on call thread
                    //а что произойдет, если в adapter.fromJson() придет пустая строка,
                    // которая тут может быть получена в элвис-операторе?
                    _storedValue = runBlocking(Dispatchers.IO) { flowValue.first() } ?: ""
                }

                return adapter.fromJson(_storedValue!!)
            }

            override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
                _storedValue = adapter.toJson(value)
                //set non blocking on Coroutine
                thisRef.scope.launch {
                    thisRef.dataStore.edit { prefs ->
                        prefs[key] = adapter.toJson(value)
                    }
                }
            }
        }
    }

}