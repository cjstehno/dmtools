package com.stehno.dmt.spellbooks.ui

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback

class BooleanCellFactory<S, T> : Callback<TableColumn<S, T>, TableCell<S, T>> {

    override fun call(param: TableColumn<S, T>?): TableCell<S, T> {
        return object : TableCell<S, T>() {
            override fun updateItem(item: T?, empty: Boolean) {
                super.updateItem(item, empty)

                val value = item as Boolean?

                text = when (empty) {
                    true -> ""
                    else -> when (value) {
                        true -> "Y"
                        else -> "N"
                    }
                }
            }
        }
    }
}