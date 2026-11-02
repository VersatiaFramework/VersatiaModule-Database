package com.github.marcoral.versatia.database

import com.github.marcoral.versatia.database.mapping.ColumnCodec
import com.github.marcoral.versatia.database.store.KeyedStore
import com.github.marcoral.versatia.database.store.PlayerStore
import com.github.marcoral.versatia.database.store.StoreSchema
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import org.bukkit.entity.Player

/**
 * The database of the plugin that injects it, configured by that plugin's `database.yml` (a SQLite
 * file in its data folder when there is none), with the migrations from its JAR applied. Every
 * plugin gets its own instance, closed after the plugin's `PLUGIN_STOP` functions. Calls that reach
 * the database block; [async] is the way into the background. See README.md.
 */
interface Database : SqlScope {
    /** The engine in use; `engine: mysql` reaching a MariaDB server reports [Engine.MARIADB]. */
    val engine: Engine

    /** What `{prefix}` expands to and what builder and store table names get: `table-prefix` of `database.yml`, else the friendly name in snake_case plus `_`. */
    val tablePrefix: String

    /** Pool, queue, schema and store figures at this moment; never touches the database. */
    val status: DatabaseStatus

    /** Runs [task] on this plugin's database threads (inline when already on one). */
    fun <T> async(task: Database.() -> T): DatabaseFuture<T>

    /** A write-behind cache of rows of [table] as [type], keyed by its constructor property [key] and loaded on demand; [create] makes the value of a key with no row. */
    fun <K : Any, V : Any> store(type: KClass<V>, table: String, key: KProperty1<V, K>, create: (K) -> V): KeyedStore<K, V>

    /** A keyed store whose reading and writing [schema] does itself. */
    fun <K : Any, V : Any> store(schema: StoreSchema<K, V>): KeyedStore<K, V>

    /**
     * One row of [table] per player: loaded at login, [onJoin] applied on join, [onLeave] on quit and at disable; saved
     * on quit, by autosave and at disable. Players already online when the store starts are loaded without [onJoin].
     */
    fun <V : Any> playerStore(
        type: KClass<V>,
        table: String,
        key: KProperty1<V, UUID>,
        onJoin: ((Player, V) -> V)? = null,
        onLeave: ((Player, V) -> V)? = null,
        create: (UUID) -> V,
    ): PlayerStore<V>

    /** A player store whose reading and writing [schema] does itself. */
    fun <V : Any> playerStore(schema: StoreSchema<UUID, V>, onJoin: ((Player, V) -> V)? = null, onLeave: ((Player, V) -> V)? = null): PlayerStore<V>

    /**
     * Binds and reads [type] through [codec] in every statement, row and store of this plugin, subtypes included (the
     * nearest registered supertype wins); call it in a bean constructor, before first use.
     */
    fun <T : Any> registerType(type: KClass<T>, codec: ColumnCodec<T, *>)
}
