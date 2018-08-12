package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.data.Events
import com.stehno.dmt.spellbooks.data.StoreService
import com.stehno.dmt.spellbooks.dsl.Spell
import com.stehno.dmt.spellbooks.event.EventBus
import javafx.scene.control.TableView

class SpellListController(private val storeService: StoreService, private val eventBus: EventBus) {

    lateinit var spellTable: TableView<Spell>

    fun initialize() {

        // TODO: cell factory to render the ritual as a checkmark or somthing
        // TODO: support sorting (resizing cols?)
        // TODO: double-click to open spell in tab
        // TODO: allow app resizing?

        spellTable.items = storeService.listSpells()

        spellTable.columns[0].cellFactory

        eventBus.subscribe(Events.SPELLS_CHANGED) { evt ->
            // update the table content
            spellTable.items = storeService.listSpells()
        }
    }
}
