package dev.versatia.database.store

import dev.versatia.database.SqlScope

/** How a store reads and writes values when a table store does not fit (several tables, a custom layout). Called on database threads. */
interface StoreSchema<K : Any, V : Any> {
    /** A name for logs, the status and errors. */
    val name: String

    /** The stored value of [key], or null when there is no row. */
    fun load(sql: SqlScope, key: K): V?

    /** The stored values of [keys]; override it to read them in one query. */
    fun loadAll(sql: SqlScope, keys: Collection<K>): Map<K, V> = keys.mapNotNull { key -> load(sql, key)?.let { key to it } }.toMap()

    /** The value of a key with no row. */
    fun create(key: K): V

    /** Writes [value]; [previous] is the last persisted value (null when never persisted), for partial updates. */
    fun save(sql: SqlScope, key: K, value: V, previous: V?)

    /** Writes [changes] inside one transaction; override it to batch. */
    fun saveAll(sql: SqlScope, changes: List<StoreChange<K, V>>) = changes.forEach { save(sql, it.key, it.value, it.previous) }

    /** Deletes the row of [key]. */
    fun delete(sql: SqlScope, key: K): Unit = throw UnsupportedOperationException("$name cannot delete; override StoreSchema.delete")
}

/** One changed entry: its key, the value to write and the last persisted value. */
data class StoreChange<K : Any, V : Any>(val key: K, val value: V, val previous: V?)
