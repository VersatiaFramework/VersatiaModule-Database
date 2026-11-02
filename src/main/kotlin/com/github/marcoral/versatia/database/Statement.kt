package com.github.marcoral.versatia.database

import kotlin.reflect.KType
import kotlin.time.Duration

/**
 * One SQL statement: bind parameters, then call one terminal function, which blocks. Not
 * thread-safe; make one per use. An unbound or unknown parameter fails before anything runs.
 */
interface Statement {
    /** The SQL as written, before tokens and parameters are expanded. */
    val sql: String

    /** Binds [value] to every `:name` as [type]; a collection or array expands `IN (:name)`, and an empty one is an error. */
    fun bind(name: String, value: Any?, type: KType): Statement

    /** Binds every entry of [values] by its runtime type. */
    fun bindAll(values: Map<String, Any?>): Statement

    /** Binds the properties of the data class [source] to the parameters named after them, camelCase or snake_case. */
    fun bindProperties(source: Any): Statement

    /** Overrides `query-timeout` for this statement; [Duration.ZERO] means no limit. */
    fun timeout(timeout: Duration): Statement

    /** Every row as [type]: a scalar from a single column, otherwise a class mapped by constructor parameter names. */
    fun <T> list(type: KType): List<T>

    /** Every row through [mapper]; the [Row] is valid only inside it. */
    fun <T> list(mapper: (Row) -> T): List<T>

    /** The one row as [type]; a [MappingException] when there are none or several. */
    fun <T> single(type: KType): T

    fun <T> single(mapper: (Row) -> T): T

    /** The only row as [type], or null when there is none; several rows are a [MappingException]. */
    fun <T : Any> singleOrNull(type: KType): T?

    fun <T : Any> singleOrNull(mapper: (Row) -> T): T?

    /** Streams the rows without collecting them. */
    fun forEach(action: (Row) -> Unit)

    /** True when the query returns at least one row. */
    fun exists(): Boolean

    /** Runs an INSERT, UPDATE or DELETE; returns the affected row count. */
    fun update(): Int

    /** Runs an INSERT and returns the generated [keyColumn] (RETURNING or generated keys, per engine). */
    fun updateAndGetKey(keyColumn: String = "id"): Long

    /** Runs any statement, DDL included, ignoring its result. */
    fun execute()

    /** Runs the statement once per item in one JDBC batch, binding each with [bind]; returns the affected counts. */
    fun <T : Any> batch(items: Iterable<T>, bind: Statement.(T) -> Unit = { bindProperties(it) }): IntArray
}
