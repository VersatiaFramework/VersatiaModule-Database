package dev.versatia.database

import java.util.concurrent.CompletableFuture

/**
 * The result of database work off the server thread. A failure that no [onFailureSync] handles is
 * logged one tick later with its SQL and the line that submitted the task. Server-thread steps are
 * dropped once the plugin is disabled.
 */
interface DatabaseFuture<T> {
    val isDone: Boolean

    /** Continues with the result on the database threads. */
    fun <R> then(next: Database.(T) -> R): DatabaseFuture<R>

    /** Continues with the result on the server thread; skipped when an earlier step failed. */
    fun <R> thenSync(action: (T) -> R): DatabaseFuture<R>

    /** Runs [action] on the server thread with the unwrapped failure of this or an earlier step, and marks it handled. */
    fun onFailureSync(action: (Throwable) -> Unit): DatabaseFuture<T>

    /** Waits for the result: guarded like any blocking call and refused on a database thread while not done; a done future returns at once anywhere. */
    fun join(): T

    /** The same result as a plain future, for APIs that take one. */
    fun toCompletableFuture(): CompletableFuture<T>
}
