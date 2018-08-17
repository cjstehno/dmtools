package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.config.ViewResolver
import com.stehno.dmt.spellbooks.data.Events
import com.stehno.dmt.spellbooks.data.StoreService
import com.stehno.dmt.spellbooks.dsl.Caster
import com.stehno.dmt.spellbooks.event.Event
import com.stehno.dmt.spellbooks.event.EventBus
import javafx.event.ActionEvent
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.Dialog
import javafx.scene.control.Menu
import javafx.stage.FileChooser


class MainController(private val viewResolver: ViewResolver, private val storeService: StoreService, private val eventBus: EventBus) {

    lateinit var booksMenu: Menu

    fun initialize() {
        updateBookMenu()

        eventBus.subscribe(Events.SPELLS_CHANGED) {
            updateBookMenu()
        }
    }

    private fun updateBookMenu() {
        booksMenu.items.clear()
        booksMenu.items.addAll(storeService.listBooks().map { book ->
            val item = CheckMenuItem(book)
            item.isSelected = true
            item.setOnAction {
                eventBus.publish(Event(Events.BOOK_TOGGLED, mapOf("book" to book, "enabled" to item.isSelected)))
            }
            item
        })
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

    fun onAbout(evt: ActionEvent) {
        val alert = Alert(AlertType.INFORMATION)
        alert.title = "About"
        alert.headerText = "Spellbook Manager\nhttps://github.com/cjstehno/dmtools/tree/master/spellbooks"

        val stats = storeService.stats()

        alert.contentText = "Managing ${storeService.count()} spells:\n" +
            "Bard: ${stats[Caster.BARD]}\n" +
            "Cleric: ${stats[Caster.CLERIC]}\n" +
            "Druid: ${stats[Caster.DRUID]}\n" +
            "Paladin: ${stats[Caster.PALADIN]}\n" +
            "Ranger: ${stats[Caster.RANGER]}\n" +
            "Sorcerer: ${stats[Caster.SORCERER]}\n" +
            "Warlock: ${stats[Caster.WARLOCK]}\n" +
            "Wizard: ${stats[Caster.WIZARD]}\n"

        alert.showAndWait()
    }

    fun onColumnSelection(evt: ActionEvent) {
        val menuItem = evt.source as CheckMenuItem
        eventBus.publish(Event(Events.COLUMNS_CHANGED, mapOf("column" to menuItem.text, "enabled" to menuItem.isSelected)))
    }
}