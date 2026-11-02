package com.github.marcoral.versatia.database

import java.sql.SQLException

/** Every failure of the module; messages start with `Plugin <name>:` and say what to do. */
open class DatabaseException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/** `database.yml` or `connections.yml` is invalid, an SQL file in the JAR is malformed, or the server is too old; names the file and the key. */
class DatabaseConfigException(message: String, cause: Throwable? = null) : DatabaseException(message, cause)

/** No connection could be made or borrowed in time; the message carries the fix for the admin. */
class DatabaseUnavailableException(message: String, cause: Throwable? = null) : DatabaseException(message, cause)

/** More background tasks are waiting than `queue-limit` allows. */
class DatabaseBusyException(message: String) : DatabaseException(message)

/** The plugin, or the module, is disabling or disabled. */
class DatabaseClosedException(message: String) : DatabaseException(message)

/** A blocking call on the server thread while `main-thread: throw`. */
class MainThreadException(message: String) : DatabaseException(message)

/** A placeholder or parameter problem: an unknown `{token}`, an unbound or unknown `:name`, an empty `IN` collection. */
class SqlTemplateException(message: String) : DatabaseException(message)

/** [SqlScope.named] got a name no `database/queries` file defines; lists close names. */
class UnknownQueryException(message: String) : DatabaseException(message)

/** A row could not become the requested type, or a single-row call found none or several. */
class MappingException(message: String) : DatabaseException(message)

/** A migration or an import failed, was changed after it was applied, or the schema does not match a store's class. */
class MigrationException(message: String, cause: Throwable? = null) : DatabaseException(message, cause)

/** The database rejected a statement; [sql] is the statement as written, without values. */
open class QueryException(
    message: String,
    val sql: String,
    val sqlState: String?,
    val errorCode: Int,
    val kind: FailureKind,
    cause: SQLException,
) : DatabaseException(message, cause)

/** A primary key or unique constraint was violated, recognised the same way on every engine. */
class DuplicateKeyException(message: String, sql: String, sqlState: String?, errorCode: Int, cause: SQLException) :
    QueryException(message, sql, sqlState, errorCode, FailureKind.DUPLICATE_KEY, cause)

/** Portable classification of a statement failure; [retryable] ones are rerun by `transaction(retries = n)`. */
enum class FailureKind(val retryable: Boolean) {
    DUPLICATE_KEY(false), FOREIGN_KEY(false), NOT_NULL(false), CHECK(false),
    DEADLOCK(true), LOCK_TIMEOUT(true), TIMEOUT(false), CONNECTION(false), OTHER(false),
}
