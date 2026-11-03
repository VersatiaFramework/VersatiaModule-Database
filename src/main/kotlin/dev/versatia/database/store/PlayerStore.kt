package dev.versatia.database.store

import dev.versatia.database.DatabaseFuture
import java.util.UUID
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

/** A store keyed by player UUID: loaded before join (the login is refused when that fails), saved on quit, flushed at disable. */
interface PlayerStore<V : Any> : KeyedStore<UUID, V> {
    /** The value of an online [player]; an [EntryNotLoadedException] for a player who never logged in (an NPC, say). */
    operator fun get(player: Player): V

    operator fun set(player: Player, value: V)

    fun update(player: Player, change: (V) -> V): V

    fun tryUpdate(player: Player, change: (V) -> V?): V?

    /** [KeyedStore.transaction] for players, online or not. */
    fun <R> transaction(vararg players: OfflinePlayer, retries: Int = 0, block: StoreTransaction<UUID, V>.() -> R): DatabaseFuture<R>
}
