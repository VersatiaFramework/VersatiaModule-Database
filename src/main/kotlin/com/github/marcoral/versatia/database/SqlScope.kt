package com.github.marcoral.versatia.database

import java.sql.Connection

/**
 * What runs SQL: the database, or a transaction. Every call blocks. On a thread inside a
 * transaction (or a [connection] block), calls made through any scope of the same database join
 * that connection.
 */
interface SqlScope {
    /** A statement from [sql] with `:named` parameters and `{tokens}`; [parameters] are bound by their runtime type. */
    fun sql(sql: String, vararg parameters: Pair<String, Any?>): Statement

    /** The `-- name: <name>` block of the plugin's `database/queries` files; an engine override wins. */
    fun named(name: String, vararg parameters: Pair<String, Any?>): Statement

    /** Inserts [row] (a data class or a `Map` of columns) into [table] (prefixed); returns the rows inserted. */
    fun insert(table: String, row: Any): Int

    /** Inserts [row] and returns the generated [keyColumn]; a [keyColumn] property holding 0 or null is left out of the insert. */
    fun insertAndGetKey(table: String, row: Any, keyColumn: String = "id"): Long

    /** Inserts the data class [row] and returns a copy of it with the generated [keyColumn] property filled in; see [insertAndGetKey]. */
    fun <T : Any> insertReturning(table: String, row: T, keyColumn: String = "id"): T

    /** Inserts [row], or updates its other columns when a row with the same [key] (and [moreKeys]) columns exists; returns 1. */
    fun upsert(table: String, row: Any, key: String, vararg moreKeys: String): Int

    /**
     * Runs [block] in a transaction: committed when it returns, rolled back when it throws, a
     * savepoint when nested; the outermost one reruns up to [retries] times after a deadlock or a
     * serialization failure.
     */
    fun <T> transaction(retries: Int = 0, block: Transaction.() -> T): T

    /** Raw JDBC on the current or a borrowed connection; do not close it or change auto-commit. */
    fun <T> connection(block: (Connection) -> T): T
}
