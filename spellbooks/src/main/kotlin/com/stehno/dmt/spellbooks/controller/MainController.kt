package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.config.ViewResolver
import javafx.event.ActionEvent
import javafx.scene.control.Dialog
import javafx.stage.FileChooser

class MainController(private val viewResolver: ViewResolver) {

    fun initialize() {

    }

    fun onImport(event: ActionEvent) {
        val selectedFile = FileChooser().apply {
            title = "Import Spells"
            selectedExtensionFilter = FileChooser.ExtensionFilter("Spellbooks", mutableListOf("*.tome", "*.codex"))
        }.showOpenDialog(null)

        if (selectedFile.extension in listOf("tome", "codex")) {
            val viewAndController = viewResolver.resolveAndController<Dialog<Void>, ImportProgressDialogController>("/ui/import_progress_dialog.fxml")
            viewAndController.second.load(selectedFile)
            viewAndController.first.showAndWait()
        }
    }
}