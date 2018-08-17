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
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.util.StringConverter

class SpellListController(private val storeService: StoreService, private val eventBus: EventBus) {

    // TODO: support sorting (resizing cols?)
    // TODO: add ability to add more columns (or select visible - save as pref)

    lateinit var levelFilter: ChoiceBox<SpellLevel>
    lateinit var schoolFilter: ChoiceBox<School>
    lateinit var casterFilter: ChoiceBox<Caster>
    lateinit var searchFilter: TextField
    lateinit var footerLabel: Label
    lateinit var spellTable: TableView<Spell>

    private var searchString = ""
    private val books = mutableMapOf<String, Boolean>()

    fun initialize() {
        levelFilter.items = observableArrayList(listOf(null, *SpellLevel.values()))
        levelFilter.selectionModel.select(0)

        schoolFilter.items = observableArrayList(listOf(null, *School.values()))
        schoolFilter.selectionModel.select(0)

        casterFilter.items = observableArrayList(listOf(null, *Caster.values()))
        casterFilter.selectionModel.select(0)

        searchFilter.textProperty().addListener { observable, oldValue, newValue ->
            searchString = newValue
            updateItems()
        }

        storeService.listBooks().forEach { book ->
            books[book] = true
        }

        eventBus.subscribe(Events.BOOK_TOGGLED) { evt ->
            books[evt.payload["book"].toString()] = evt.payload["enabled"] as Boolean
            updateItems()
        }

        updateItems()

        eventBus.subscribe(Events.SPELLS_CHANGED) { evt ->
            updateItems()
        }
    }

    fun updateItems() {
        val enabledBooks = books.filter { ent-> ent.value }.map { ent-> ent.key }

        spellTable.items = storeService.listSpells(SpellFilter(
            levelFilter.selectionModel.selectedItem,
            schoolFilter.selectionModel.selectedItem,
            casterFilter.selectionModel.selectedItem,
            searchString,
            enabledBooks
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

class SpellLevelStringConverter : StringConverter<SpellLevel>() {
    override fun fromString(lvl: String?): SpellLevel? {
        return if (lvl == "All Levels") {
            null
        } else {
            SpellLevel.from(lvl!!)
        }
    }

    override fun toString(lvl: SpellLevel?) = if (lvl != null) {
        when (lvl.level) {
            0 -> lvl.label
            else -> "${lvl.label} Level"
        }
    } else {
        "All Levels"
    }
}

class SchoolStringConverter : StringConverter<School>() {
    override fun fromString(school: String?): School? {
        return if (school == "All Schools") {
            null
        } else {
            School.from(school!!)
        }
    }

    override fun toString(school: School?) = school?.toString() ?: "All Schools"
}

class CasterStringConverter : StringConverter<Caster>() {

    override fun fromString(caster: String?): Caster? = if (caster == "All Casters") {
        null
    } else {
        Caster.from(caster!!)
    }

    override fun toString(caster: Caster?) = caster?.toString() ?: "All Casters"
}