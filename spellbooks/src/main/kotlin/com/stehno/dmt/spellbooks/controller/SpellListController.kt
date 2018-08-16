package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.data.Events
import com.stehno.dmt.spellbooks.data.StoreService
import com.stehno.dmt.spellbooks.dsl.Spell
import com.stehno.dmt.spellbooks.event.Event
import com.stehno.dmt.spellbooks.event.EventBus
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent

class SpellListController(private val storeService: StoreService, private val eventBus: EventBus) {

    lateinit var spellTable: TableView<Spell>

    fun initialize() {
        // TODO: support sorting (resizing cols?)
        // TODO: add ability to add more columns (or select visible - save as pref)

        spellTable.items = storeService.listSpells()

        eventBus.subscribe(Events.SPELLS_CHANGED) { evt ->
            // update the table content
            spellTable.items = storeService.listSpells()
        }
    }

    fun onTableClicked(me: MouseEvent) {
        if (me.clickCount >= 2) {
            val selectedSpell = spellTable.selectionModel.selectedItem
            eventBus.publish(Event(Events.SHOW_SPELL_DETAILS, mapOf(Pair("key", selectedSpell.key))))
        }
    }
}
