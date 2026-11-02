package com.github.marcoral.versatia.database.mapping

import kotlin.reflect.KClass

/**
 * Stores [T] as [S], a type the module binds itself: `String`, `Int`, `Long`, `Double`, `Boolean`,
 * `ByteArray`, `UUID`, `Instant` or `Json`. An `object` or a class with a no-arg constructor.
 */
interface ColumnCodec<T : Any, S : Any> {
    val storedAs: KClass<S>
    fun encode(value: T): S
    fun decode(stored: S): T
}

/** The "no codec" default of [Column.codec]. */
object NoCodec : ColumnCodec<Any, Any> {
    override val storedAs = Any::class
    override fun encode(value: Any) = value
    override fun decode(stored: Any) = stored
}
