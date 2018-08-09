package com.stehno.dmt.difficulty.controller

import com.stehno.dmt.difficulty.Store
import com.stehno.dmt.difficulty.config.ViewResolver
import com.stehno.dmt.difficulty.model.Challenger
import javafx.event.ActionEvent
import javafx.scene.control.ContextMenu
import javafx.scene.control.Dialog
import javafx.scene.control.ListView
import javafx.scene.control.MenuItem
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.MouseEvent

class ChallengeListController(
    private val store: Store,
    private val viewResolver: ViewResolver) {

    lateinit var challengerList: ListView<Challenger>

    fun initialize() {
        val contextMenu = buildContextMenu()

        challengerList.items = store.challengers()
        challengerList.setOnContextMenuRequested { evt: ContextMenuEvent ->
            contextMenu.show(challengerList, evt.screenX, evt.screenY)
        }

        challengerList.setOnMouseClicked { evt: MouseEvent ->
            if( evt.clickCount >= 2 ){
                editChallenger(null)
            }
        }
    }

    fun addChallenger(event: ActionEvent) {
        viewResolver.resolve<Dialog<Challenger>>("/ui/challenger_dialog.fxml").showAndWait().ifPresent { chal ->
            store.addChallenger(chal)
        }
    }

    fun removeChallenger(event: ActionEvent) {
        challengerList.selectionModel.selectedItems.forEach { ch ->
            store.removeChallenger(ch.challenge)
        }
    }

    // TODO: add double-click to edit

    fun editChallenger(event: ActionEvent?){
        val selected = challengerList.selectionModel.selectedItems.first()

        val viewAndController = viewResolver.resolveAndController<Dialog<Challenger>, ChallengerDialogController>("/ui/challenger_dialog.fxml")

        viewAndController.second.populate(selected)

        viewAndController.first.showAndWait().ifPresent { chal ->
            store.updateChallenger(chal)
        }
    }

    private fun buildContextMenu(): ContextMenu {
        // FIXME: pull this into xml

        val contextMenu = ContextMenu()

        val addContextItem = MenuItem("Add")
        addContextItem.setOnAction(this::addChallenger)

        val removeContextItem = MenuItem("Remove")
        removeContextItem.setOnAction(this::removeChallenger)

        val editContextItem = MenuItem("Edit")
        editContextItem.setOnAction(this::editChallenger)

        contextMenu.items.addAll(addContextItem, editContextItem, removeContextItem)

        return contextMenu
    }
}