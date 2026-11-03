package dev.versatia.database

import dev.versatia.database.mapping.ColumnCodec
import java.util.UUID
import kotlin.reflect.KProperty1
import org.bukkit.entity.Player

/** `database.store("guilds", Guild::id) { Guild(it) }`. */
inline fun <K : Any, reified V : Any> Database.store(table: String, key: KProperty1<V, K>, noinline create: (K) -> V) =
    store(V::class, table, key, create)

/** `database.playerStore("profiles", Profile::uuid) { Profile(it) }`. */
inline fun <reified V : Any> Database.playerStore(
    table: String,
    key: KProperty1<V, UUID>,
    noinline onJoin: ((Player, V) -> V)? = null,
    noinline onLeave: ((Player, V) -> V)? = null,
    noinline create: (UUID) -> V,
) = playerStore(V::class, table, key, onJoin, onLeave, create)

/** `database.registerType(LocationText)`. */
inline fun <reified T : Any> Database.registerType(codec: ColumnCodec<T, *>) = registerType(T::class, codec)
