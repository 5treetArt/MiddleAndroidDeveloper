package ru.skillbranch.skillarticles.data.adapters

import org.json.JSONObject
import ru.skillbranch.skillarticles.data.local.User
import ru.skillbranch.skillarticles.extensions.asMap

class UserJsonAdapter : JsonAdapter<User> {
    override fun fromJson(json: String): User? {
        val jsonObj = JSONObject(json)

        val id: String? = jsonObj.getString("id")
        val name: String? = jsonObj.getString("name")
        val avatar: String? = jsonObj.getString("avatar")
        //если есть необходимость в элвис-операторе, то нужно,
        // чтобы выражение в левой части было nullable.
        val rating: Int = jsonObj.getString("rating").toIntOrNull() ?: 0
        val respect: Int = jsonObj.getString("respect").toIntOrNull() ?: 0
        val about: String? = jsonObj.getString("about")
        return if (id != null && name != null) User(id, name, avatar, rating, respect, about)
        else null
    }

    override fun toJson(obj: User?): String {
        //Map.toString возвращает строку вида { "a"="a", ... }
        // (что можно посмотреть в исходниках abstractMap например),
        // а для формата JSON требуется, чтобы объект был сериализован в виде { "a": "a", ... }
        // соответственно, объект сериализованный таким образом скорее всего корректно не распарсится

        //скорее всего так будет правильней
        return JSONObject(obj?.asMap().orEmpty()).toString()
    }
}