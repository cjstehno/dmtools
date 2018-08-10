package com.stehno.dmt.difficulty.controller

import com.stehno.dmt.difficulty.Store
import com.stehno.dmt.difficulty.model.Difficulty
import javafx.scene.control.TableView

class DifficultyTableController(private val store: Store) {

    lateinit var difficultyTable: TableView<Difficulty>

    fun initialize() {
        difficultyTable.items = store.difficulties()
    }
}