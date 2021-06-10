package ru.skillbranch.skillarticles.data.adapters

//нет необходимости делать интерфейсы публичными, так как в котлине они по умолчанию публичны.
interface JsonAdapter<T>{
    fun fromJson (json : String) : T?
    fun toJson (obj:T?) : String
}