package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.config.ViewResolver
import com.stehno.dmt.spellbooks.data.Events
import com.stehno.dmt.spellbooks.data.StoreService
import com.stehno.dmt.spellbooks.event.EventBus
import javafx.scene.control.Tab
import javafx.scene.control.TabPane

class SpellDisplayContainerController(val eventBus: EventBus, val viewResolver: ViewResolver, val storeService: StoreService) {

    lateinit var tabPane: TabPane

    fun initialize() {
        eventBus.subscribe(Events.SHOW_SPELL_DETAILS) { evt ->
            val spell = storeService.fetchSpell(evt.payload.get("key").toString())

            val tabAndController = viewResolver.resolveAndController<Tab, SpellDetailsTabController>("/ui/spell_details_tab.fxml")
            tabAndController.second.spell(spell)
            tabPane.tabs.add(tabAndController.first)
            tabPane.selectionModel.select(tabAndController.first)
        }
    }
}