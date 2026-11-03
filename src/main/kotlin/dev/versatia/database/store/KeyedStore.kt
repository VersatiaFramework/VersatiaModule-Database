package dev.versatia.database.store

import dev.versatia.database.DatabaseFuture

/**
 * A write-behind cache of immutable values: replacing a value marks it changed, and saves write
 * only the changed columns. [get], [set], [update] and [tryUpdate] touch memory only and are safe
 * from any thread; the others reach the database off the calling thread and return futures.
 */
interface KeyedStore<K : Any, V : Any> {
    /** The table name, or the schema's name; used in logs and the status. */
    val name: String

    /** The cached value, or null when [key] is not loaded. */
    operator fun get(key: K): V?

    /** The cached value; an [EntryNotLoadedException] when [key] is not loaded. */
    fun getValue(key: K): V

    operator fun contains(key: K): Boolean

    /** The keys loaded right now. */
    val keys: Set<K>

    /** Replaces the value of [key]; an unloaded key becomes a new entry, written with an upsert at the next save. */
    operator fun set(key: K, value: V)

    /** Replaces the loaded value with [change] of it and returns the new value; applied again on top of a store transaction's result when one holds the key. */
    fun update(key: K, change: (V) -> V): V

    /** Like [update], but [change] may refuse by returning null; refused (null) while a store transaction holds the key. */
    fun tryUpdate(key: K, change: (V) -> V?): V?

    /** Loads [key] unless it is cached; with no row, the value from `create`. */
    fun load(key: K): DatabaseFuture<V>

    /** Writes the pending change of [key], if any, after the never-written rows of the stores this store's table references. */
    fun save(key: K): DatabaseFuture<Unit>

    /** Writes every pending change in one transaction, like [save]; returns how many entries of this store were written. */
    fun saveAll(): DatabaseFuture<Int>

    /** Saves [key] if it changed, then forgets it, unless it changed again meanwhile. */
    fun evict(key: K): DatabaseFuture<Unit>

    /** Deletes the row of [key] and forgets it, in one transaction with the rows of [key] of the stores whose key column references this store's key column. */
    fun delete(key: K): DatabaseFuture<Unit>

    /** Runs [block] in one database transaction holding [keys]: values set in it are written in it and published on commit. */
    fun <R> transaction(vararg keys: K, retries: Int = 0, block: StoreTransaction<K, V>.() -> R): DatabaseFuture<R>
}
