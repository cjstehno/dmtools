package com.stehno.dmt.spellbooks.controller

import javafx.scene.control.Tab
import javafx.scene.web.WebView

class SpellDetailsTabController {

    lateinit var detailTab: Tab
    lateinit var spellDetails: WebView

    fun initialize() {
        val spellName = "Animate Objects"
        val contentUrl = SpellDetailsTabController::class.java.getResource("/ui/spell_details.html").toString()

        detailTab.text = spellName
        spellDetails.engine.load(contentUrl)
    }
}