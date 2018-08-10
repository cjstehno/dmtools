package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.dsl.SpellbookLoader
import javafx.event.ActionEvent
import javafx.stage.FileChooser

class MainController {

    fun initialize() {

    }

    fun onImport(event: ActionEvent) {
        val selectedFile = FileChooser().apply {
            title = "Import Spells"
            selectedExtensionFilter = FileChooser.ExtensionFilter("Spellbooks", mutableListOf("*.tome", "*.codex"))
        }.showOpenDialog(null)

        // TODO: run this on a separate thread to allow UI to continue

        if( selectedFile.extension in listOf("tome", "codex")){
            val spellbook = SpellbookLoader.load(selectedFile)
            // TODO: a progress screen with scrolling list of spells as loaded...
            println("Loaded ${spellbook.spells.size} spells from ${spellbook.title}.")
        }
    }
}