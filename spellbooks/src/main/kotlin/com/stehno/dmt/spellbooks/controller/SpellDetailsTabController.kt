package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.dsl.Spell
import groovy.text.GStringTemplateEngine
import javafx.scene.control.Tab
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
}