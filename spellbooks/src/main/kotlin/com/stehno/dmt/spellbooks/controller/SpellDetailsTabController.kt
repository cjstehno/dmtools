package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.dsl.Spell
import groovy.text.GStringTemplateEngine
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.Tab
import javafx.scene.input.ContextMenuEvent
import javafx.scene.web.WebView
import org.asciidoctor.Asciidoctor

class SpellDetailsTabController {

    lateinit var detailTab: Tab
    lateinit var spellDetails: WebView

    private val template = GStringTemplateEngine().createTemplate(SpellDetailsTabController::class.java.getResource("/ui/spell_details.html"))
    private val asciidoctor = Asciidoctor.Factory.create()

    fun initialize() {
        detailTab.text = "Loading..."
    }

    fun spell(spell: Spell) {
        detailTab.text = spell.name
        spellDetails.engine.loadContent(
            template.make(mutableMapOf("spell" to spell, "adoc" to asciidoctor)).toString(),
            "text/html"
        )
    }

    fun onContextMenu(evt: ContextMenuEvent) {
/*        // FIXME: merge shared code
        val contextMenu = ContextMenu()

        val favoriteItem = MenuItem("Favorite")
        val newListItem = MenuItem("Add to New List...")

        contextMenu.items.addAll(
            favoriteItem,
            newListItem
        )

        contextMenu.isAutoHide = true
        contextMenu.show(spellDetails, evt.screenX, evt.screenY)*/
    }
}