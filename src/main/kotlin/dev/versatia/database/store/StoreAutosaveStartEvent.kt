package dev.versatia.database.store

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin

/**
 * An autosave of [plugin]'s stores is about to write [dirty] changed entries of [stores]; called on the server thread,
 * only when something changed, and never once [plugin] is disabled. A [StoreAutosaveEndEvent] follows.
 */
class StoreAutosaveStartEvent(val plugin: Plugin, val stores: List<String>, val dirty: Int) : Event() {
    override fun getHandlers() = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
