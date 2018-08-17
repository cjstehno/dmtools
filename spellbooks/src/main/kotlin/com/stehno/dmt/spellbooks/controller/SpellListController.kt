package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.data.Events
import com.stehno.dmt.spellbooks.data.SpellFilter
import com.stehno.dmt.spellbooks.data.StoreService
import com.stehno.dmt.spellbooks.dsl.Caster
import com.stehno.dmt.spellbooks.dsl.School
import com.stehno.dmt.spellbooks.dsl.Spell
import com.stehno.dmt.spellbooks.dsl.SpellLevel
import com.stehno.dmt.spellbooks.event.Event
import com.stehno.dmt.spellbooks.event.EventBus
import com.stehno.dmt.spellbooks.ui.BooleanCellFactory
import javafx.collections.FXCollections.observableArrayList
import javafx.event.ActionEvent
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseEvent

class SpellListController(private val storeService: StoreService, private val eventBus: EventBus) {

    // TODO: support sorting (resizing cols?)

    lateinit var levelFilter: ChoiceBox<SpellLevel>
    lateinit var schoolFilter: ChoiceBox<School>
    lateinit var casterFilter: ChoiceBox<Caster>
    lateinit var searchFilter: TextField
    lateinit var footerLabel: Label
    lateinit var spellTable: TableView<Spell>
    lateinit var guildToggle: ToggleButton

    private var searchString = ""
    private val books = mutableMapOf<String, Boolean>()

    fun initialize() {
        levelFilter.items = observableArrayList(listOf(null, *SpellLevel.values()))
        levelFilter.selectionModel.select(0)

        schoolFilter.items = observableArrayList(listOf(null, *School.values()))
        schoolFilter.selectionModel.select(0)

        casterFilter.items = observableArrayList(listOf(null, *Caster.values()))
        casterFilter.selectionModel.select(0)

        // setup the initial columns
        spellTable.columns.addAll(
            columnFor("Level"), columnFor("Name"), columnFor("School"), columnFor("Ritual")
        )

        searchFilter.textProperty().addListener { observable, oldValue, newValue ->
            searchString = newValue
            updateItems()
        }

        storeService.listBooks().forEach { book ->
            books[book] = true
        }

        updateItems()

        eventBus.subscribe(Events.BOOK_TOGGLED) { evt ->
            books[evt.payload["book"].toString()] = evt.payload["enabled"] as Boolean
            updateItems()
        }

        eventBus.subscribe(Events.SPELLS_CHANGED) { evt ->
            updateItems()
        }

        eventBus.subscribe(Events.COLUMNS_CHANGED) { evt ->
            val columnName = evt.payload["column"] as String
            val enabled = evt.payload["enabled"] as Boolean

            if (enabled) {
                spellTable.columns.add(columnFor(columnName))
            } else {
                spellTable.columns.remove(spellTable.columns.find { col -> col.text == columnName })
            }
        }
    }

    private fun columnFor(columnName: String): TableColumn<Spell, *> {
        return when (columnName) {
            "Name" -> TableColumn<Spell, String>().apply {
                text = "Name"
                cellValueFactory = PropertyValueFactory<Spell, String>("name")
            }
            "Book" -> TableColumn<Spell, String>().apply {
                text = "Book"
                cellValueFactory = PropertyValueFactory<Spell, String>("book")
            }
            "Ritual" -> TableColumn<Spell, String>().apply {
                text = "Ritual"
                cellValueFactory = PropertyValueFactory<Spell, String>("ritual")
                cellFactory = BooleanCellFactory<Spell, String>()
            }
            "Level" -> TableColumn<Spell, String>().apply {
                text = "Level"
                cellValueFactory = PropertyValueFactory<Spell, String>("level")
            }
            "School" -> TableColumn<Spell, String>().apply {
                text = "School"
                cellValueFactory = PropertyValueFactory<Spell, String>("school")
            }
            "Casting Time" -> TableColumn<Spell, String>().apply {
                text = "Casting Time"
                cellValueFactory = PropertyValueFactory<Spell, String>("castingTime")
            }
            "Range" -> TableColumn<Spell, String>().apply {
                text = "Range"
                cellValueFactory = PropertyValueFactory<Spell, String>("range")
            }
            "Duration" -> TableColumn<Spell, String>().apply {
                text = "Duration"
                cellValueFactory = PropertyValueFactory<Spell, String>("duration")
            }
            "Components" -> TableColumn<Spell, String>().apply {
                text = "Components"
                cellValueFactory = PropertyValueFactory<Spell, String>("components")
            }
            else -> throw IllegalArgumentException("Unexpected column ($columnName)")
        }
    }

    fun updateItems() {
        val enabledBooks = books.filter { ent -> ent.value }.map { ent -> ent.key }

        spellTable.items = storeService.listSpells(SpellFilter(
            levelFilter.selectionModel.selectedItem,
            schoolFilter.selectionModel.selectedItem,
            casterFilter.selectionModel.selectedItem,
            searchString,
            enabledBooks,
            guildToggle.isSelected
        ))

        footerLabel.text = "Showing ${spellTable.items.size} of ${storeService.count()} spells"
    }

    fun onTableClicked(me: MouseEvent) {
        if (me.clickCount >= 2) {
            val selectedSpell = spellTable.selectionModel.selectedItem
            eventBus.publish(Event(Events.SHOW_SPELL_DETAILS, mapOf(Pair("key", selectedSpell.key))))
        }
    }
}
