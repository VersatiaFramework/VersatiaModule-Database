package com.github.marcoral.versatia.database

/**
 * A snapshot of one plugin's database: [plugin] is its friendly name, [target] a file path or `host:port/database`, never
 * credentials, and [schemaVersion] the highest applied migration (0 without migrations or before the database opens).
 */
data class DatabaseStatus(
    val plugin: String,
    val engine: Engine,
    val target: String,
    val open: Boolean,
    val schemaVersion: Int,
    val pool: PoolStatus?,
    val queuedTasks: Int,
    val runningTasks: Int,
    val failedTasks: Long,
    val slowStatements: Long,
    val stores: List<StoreStatus>,
)

/** Pool figures; [shared] when the pool belongs to a connection from `connections.yml`. */
data class PoolStatus(val name: String, val shared: Boolean, val active: Int, val idle: Int, val waiting: Int, val max: Int)

/** One store's entries and save state. */
data class StoreStatus(val name: String, val loaded: Int, val dirty: Int, val lastSaveFailed: Boolean)
