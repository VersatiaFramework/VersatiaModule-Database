package dev.versatia.database.store

import kotlin.time.Duration
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin

/**
 * The autosave announced by [StoreAutosaveStartEvent] ended after [duration]: [saved] entries were written and [failed]
 * stay changed for the next one, the first [failure] says why. Called on the server thread, never once [plugin] is disabled.
 */
class StoreAutosaveEndEvent(val plugin: Plugin, val saved: Int, val failed: Int, val duration: Duration, val failure: Throwable?) : Event() {
    override fun getHandlers() = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
