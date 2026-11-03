package dev.versatia.database.store

import dev.versatia.database.DatabaseException

/** A value was read from a store that has not loaded its key. */
class EntryNotLoadedException(message: String) : DatabaseException(message)
