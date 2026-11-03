package dev.versatia.database

import kotlinx.coroutines.future.await

/** Suspends until the database work is done, without blocking the thread: `val top = database.async { ... }.await()`. */
suspend fun <T> DatabaseFuture<T>.await() = toCompletableFuture().await()
