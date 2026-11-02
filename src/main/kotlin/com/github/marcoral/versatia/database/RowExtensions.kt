package com.github.marcoral.versatia.database

import kotlin.reflect.typeOf

/** `val coins: Long = row["coins"]`: the type, nullability included, comes from where the value goes. */
inline operator fun <reified T> Row.get(column: String): T = get(column, typeOf<T>())

/** `row.into<Profile>()`. */
inline fun <reified T> Row.into(): T = into(typeOf<T>())
