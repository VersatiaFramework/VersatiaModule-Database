package dev.versatia.database.store

import java.util.UUID
import org.bukkit.OfflinePlayer

/** `this[victim]` in a player store's transaction. */
operator fun <V : Any> StoreTransaction<UUID, V>.get(player: OfflinePlayer) = get(player.uniqueId)

/** `this[victim] = value` in a player store's transaction. */
operator fun <V : Any> StoreTransaction<UUID, V>.set(player: OfflinePlayer, value: V) = set(player.uniqueId, value)

/** `update(killer) { it.copy(coins = it.coins + bounty) }` in a player store's transaction. */
fun <V : Any> StoreTransaction<UUID, V>.update(player: OfflinePlayer, change: (V) -> V) = update(player.uniqueId, change)
