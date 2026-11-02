package com.github.marcoral.versatia.database

import kotlin.reflect.typeOf

/** Binds with the static type of [value], so a null `UUID?` is still bound as a UUID. */
inline fun <reified T> Statement.bind(name: String, value: T) = bind(name, value, typeOf<T>())

/** `sql(...).list<TopEntry>()`; nullability and type arguments of [T] are honoured. */
inline fun <reified T> Statement.list(): List<T> = list(typeOf<T>())

/** `sql("SELECT COUNT(*) ...").single<Long>()`. */
inline fun <reified T> Statement.single(): T = single(typeOf<T>())

inline fun <reified T : Any> Statement.singleOrNull(): T? = singleOrNull(typeOf<T>())
