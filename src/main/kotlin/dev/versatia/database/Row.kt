package dev.versatia.database

import dev.versatia.database.mapping.ColumnCodec
import java.time.Instant
import java.util.UUID
import kotlin.reflect.KType

/** The current row of a result, valid only inside the mapper it was passed to; columns are matched by label, ignoring case. */
interface Row {
    /** The column labels, in select order. */
    val columns: List<String>

    operator fun contains(column: String): Boolean

    /** The column as [type]; SQL NULL into a non-null type is a [MappingException] naming the column. */
    fun <T> get(column: String, type: KType): T

    /** The column decoded by [codec]; null when it is NULL. */
    fun <T : Any> get(column: String, codec: ColumnCodec<T, *>): T?

    /** The whole row as [type], mapped as [Statement.list] maps it. */
    fun <T> into(type: KType): T

    fun string(column: String): String
    fun stringOrNull(column: String): String?
    fun int(column: String): Int
    fun intOrNull(column: String): Int?
    fun long(column: String): Long
    fun longOrNull(column: String): Long?
    fun double(column: String): Double
    fun doubleOrNull(column: String): Double?
    fun boolean(column: String): Boolean
    fun booleanOrNull(column: String): Boolean?

    /** Reads a `{uuid}` column, whatever the engine stores. */
    fun uuid(column: String): UUID
    fun uuidOrNull(column: String): UUID?

    /** Reads a `{timestamp}` column, whatever the engine stores. */
    fun instant(column: String): Instant
    fun instantOrNull(column: String): Instant?
    fun bytes(column: String): ByteArray
    fun bytesOrNull(column: String): ByteArray?
}
