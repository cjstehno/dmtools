package com.stehno.dmt.difficulty.controller

import com.stehno.dmt.difficulty.model.Difficulty
import javafx.collections.FXCollections
import javafx.scene.control.TableView

class DifficultyTableController {

    lateinit var difficultyTable: TableView<Difficulty>

    fun initialize() {
        difficultyTable.items = FXCollections.observableArrayList(
            Difficulty("Tier 1", ""),
            Difficulty("1", "Deadly"),
            Difficulty("2", "Hard"),
            Difficulty("3", "Medium"),
            Difficulty("4", "Medium"),
            Difficulty("Tier 2", ""),
            Difficulty("5", "Easy"),
            Difficulty("6", "Easy"),
            Difficulty("7", "Easy"),
            Difficulty("8", "Easy"),
            Difficulty("9", "Easy"),
            Difficulty("10", "Easy"),
            Difficulty("Tier 3", ""),
            Difficulty("11", "Easy"),
            Difficulty("12", "Easy"),
            Difficulty("13", "Easy"),
            Difficulty("14", "Easy"),
            Difficulty("15", "Easy"),
            Difficulty("16", "Easy"),
            Difficulty("Tier 4", ""),
            Difficulty("17", "Easy"),
            Difficulty("18", "Easy"),
            Difficulty("19", "Easy"),
            Difficulty("20", "Easy")
        )
    }
}