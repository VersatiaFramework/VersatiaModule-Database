package com.github.marcoral.versatia.database

/** A running transaction; every call through it, or through its database on the same thread, uses one connection. */
interface Transaction : SqlScope {
    /** Runs [action] on this thread after the outermost transaction commits; never after a rollback, never twice on a retry. */
    fun afterCommit(action: () -> Unit)

    /** Rolls back when the outermost block returns, without an exception. */
    fun setRollbackOnly()
}
