package com.stehno.dmt.difficulty.controller

import com.stehno.dmt.difficulty.model.Difficulty
import javafx.collections.FXCollections
import javafx.scene.control.TableView

class MainController {

    lateinit var difficultyTable: TableView<Difficulty>

    fun initialize() {
        /// FIXME: move the table to its own controller
        difficultyTable.items = FXCollections.observableArrayList(
            Difficulty("1", "Medium"),
            Difficulty("2", "Hard"),
            Difficulty("3", "Hard")
        )
    }
}
