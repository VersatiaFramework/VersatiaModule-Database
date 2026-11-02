package com.github.marcoral.versatia.database.mapping

import kotlin.reflect.KClass

/** The column a constructor parameter maps to, when it is not the parameter name in snake_case, and/or the [codec] that stores its type. */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Column(val name: String = "", val codec: KClass<out ColumnCodec<*, *>> = NoCodec::class)
