package net.eduard.api.lib.database

import java.sql.Timestamp
import java.util.*

val serialization: MutableMap<Class<*>, Any.() -> String> = mutableMapOf()
val deserialization: MutableMap<Class<*>, (String) -> Any> = mutableMapOf()
val dataSize :  MutableMap<Class<*>, Int> = mutableMapOf()

inline fun <reified T> save(noinline saveAs: T.() -> String) {
    val clz = T::class.java
    serialization[clz] = saveAs as ((Any) -> String)
}
inline fun <reified T> save(size : Int, noinline saveAs: T.() -> String) {
    sizeOf<T>(size)
    save(saveAs)
}
inline fun <reified T> sizeOf(size : Int){
    val clz = T::class.java
    dataSize[clz] = size
}
inline fun <reified T> load(size : Int, noinline loadAs: (String) -> T) {
    sizeOf<T>(size)
    load(loadAs)
}
inline fun <reified T> load(noinline loadAs: (String) -> T) {
    val clz = T::class.java
    deserialization[clz] = loadAs as (String) -> Any
}

fun Date.toSQLDate() = java.sql.Date(time)
fun String.toSQLDate() = java.sql.Date.valueOf(this)
fun java.sql.Date.toDate() = Date(time)

fun javaTypes() {

    save<UUID>(100) {
        toString()
    }
    load {
        UUID.fromString(it)
    }
    save<Date>  {
        toSQLDate().toString()
    }
    load {
        it.toSQLDate().toDate()
    }
    save<java.sql.Date>  {
        toString()
    }
    load {
        it.toSQLDate()
    }
    save<Timestamp> { toString()
    }

    load {
         Timestamp.valueOf(it)
    }

}