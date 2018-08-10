package com.stehno.dmt.difficulty.controller

import com.stehno.dmt.difficulty.model.Difficulty
import javafx.collections.FXCollections
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.util.Callback

class DifficultyTableController {

    lateinit var difficultyTable: TableView<Difficulty>

    fun initialize() {
        // FIXME: can these factories be configured in the fxml?

        difficultyTable.columns[1].cellFactory = Callback<TableColumn<Difficulty, out Any>, TableCell<Difficulty, out Any>> {
            object : TableCell<Difficulty, Any>() {
                override fun updateItem(item: Any?, empty: Boolean) {
                    super.updateItem(item, empty)

                    val value = item.toString()
                    text = when (empty) {
                        true -> ""
                        else -> value
                    }

                    styleClass.add(when (item) {
                        "Deadly" -> "deadly"
                        "Hard" -> "hard"
                        "Medium" -> "medium"
                        "Easy" -> "easy"
                        else -> "separator-row"
                    })
                }
            }
        }

        difficultyTable.columns[0].cellFactory = Callback<TableColumn<Difficulty, out Any>, TableCell<Difficulty, out Any>> {
            object : TableCell<Difficulty, Any>() {
                override fun updateItem(item: Any?, empty: Boolean) {
                    super.updateItem(item, empty)

                    val value = item.toString()

                    text = when (empty) {
                        true -> ""
                        else -> value
                    }

                    if (value.startsWith("Tier")) {
                        styleClass.add("separator-row")
                    }
                }
            }
        }

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
