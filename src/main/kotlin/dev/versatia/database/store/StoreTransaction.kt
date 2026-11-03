package dev.versatia.database.store

import dev.versatia.database.Transaction

/** A transaction that also holds store entries. */
interface StoreTransaction<K : Any, V : Any> : Transaction {
    /** The current value of the held [key], loaded or created when it is not cached. */
    operator fun get(key: K): V

    /** Sets the held [key]: written in this transaction, visible in the store after commit. */
    operator fun set(key: K, value: V)

    /** Sets the held [key] to [change] of its current value and returns the new value. */
    fun update(key: K, change: (V) -> V): V = change(get(key)).also { set(key, it) }
}
