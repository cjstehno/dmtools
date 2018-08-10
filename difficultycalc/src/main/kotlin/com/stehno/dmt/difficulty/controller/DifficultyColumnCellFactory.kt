package com.stehno.dmt.difficulty.controller

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback

class DifficultyColumnCellFactory<S, T> : Callback<TableColumn<S, T>, TableCell<S, T>> {

    override fun call(param: TableColumn<S, T>?): TableCell<S, T>? {
        return object : TableCell<S, T>() {
            override fun updateItem(item: T?, empty: Boolean) {
                super.updateItem(item, empty)

                val value = item.toString()
                text = when (empty) {
                    true -> ""
                    else -> value
                }

                styleClass.clear()
                styleClass.add(when (item) {
                    "DEADLY" -> "deadly"
                    "HARD" -> "hard"
                    "MEDIUM" -> "medium"
                    "EASY" -> "easy"
                    else -> "separator-row"
                })
            }
        }
    }
}

class LevelColumnCellFactory<S, T> : Callback<TableColumn<S, T>, TableCell<S, T>> {
    override fun call(param: TableColumn<S, T>?): TableCell<S, T> {
        return object : TableCell<S, T>() {
            override fun updateItem(item: T?, empty: Boolean) {
                super.updateItem(item, empty)

                val value = item.toString()

                text = when (empty) {
                    true -> ""
                    else -> value
                }

                styleClass.clear()
                if (value.startsWith("Tier")) {
                    styleClass.add("separator-row")
                }
            }
        }
    }

}