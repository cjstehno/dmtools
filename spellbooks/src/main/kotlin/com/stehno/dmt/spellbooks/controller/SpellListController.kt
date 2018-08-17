package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.data.Events
import com.stehno.dmt.spellbooks.data.StoreService
import com.stehno.dmt.spellbooks.dsl.Caster
import com.stehno.dmt.spellbooks.dsl.School
import com.stehno.dmt.spellbooks.dsl.Spell
import com.stehno.dmt.spellbooks.dsl.SpellLevel
import com.stehno.dmt.spellbooks.event.Event
import com.stehno.dmt.spellbooks.event.EventBus
import javafx.collections.FXCollections.observableArrayList
import javafx.event.ActionEvent
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent
import javafx.util.StringConverter
import java.util.function.Predicate

class SpellListController(private val storeService: StoreService, private val eventBus: EventBus) {

    // TODO: support sorting (resizing cols?)
    // TODO: add ability to add more columns (or select visible - save as pref)

    lateinit var levelFilter: ChoiceBox<SpellLevel>
    lateinit var schoolFilter: ChoiceBox<School>
    lateinit var casterFilter: ChoiceBox<Caster>
    lateinit var spellTable: TableView<Spell>

    private var levelPredicate: Predicate<Spell> = SpellLevelPredicate.ALL_LEVELS
    private var schoolPredicate: Predicate<Spell> = SchoolPredicate.ALL_SCHOOLS
    private var casterPredicate: Predicate<Spell> = CasterPredicate.ALL_CASTERS

    fun initialize() {
        levelFilter.items = observableArrayList(listOf(null, *SpellLevel.values()))
        levelFilter.selectionModel.select(0)

        schoolFilter.items = observableArrayList(listOf(null, *School.values()))
        schoolFilter.selectionModel.select(0)

        casterFilter.items = observableArrayList(listOf(null, *Caster.values()))
        casterFilter.selectionModel.select(0)

        updateItems()

        eventBus.subscribe(Events.SPELLS_CHANGED) { evt ->
            // update the table content
            spellTable.items = storeService.listSpells()
        }
    }

    private fun updateItems() {
        spellTable.items = storeService.listSpells()
            .filtered(levelPredicate)
            .filtered(schoolPredicate)
            .filtered(casterPredicate)
    }

    fun onLevelFilterSelected(evt: ActionEvent) {
        val selected = levelFilter.selectionModel.selectedItem
        levelPredicate = when (selected) {
            null -> SpellLevelPredicate.ALL_LEVELS
            else -> SpellLevelPredicate(selected)
        }
        updateItems()
    }

    fun onSchoolFilterSelected(evt: ActionEvent) {
        val selected = schoolFilter.selectionModel.selectedItem
        schoolPredicate = when (selected) {
            null -> SchoolPredicate.ALL_SCHOOLS
            else -> SchoolPredicate(selected)
        }
        updateItems()
    }

    fun onCasterFilterSelected(evt: ActionEvent) {
        val selected = casterFilter.selectionModel.selectedItem
        casterPredicate = when (selected) {
            null -> CasterPredicate.ALL_CASTERS
            else -> CasterPredicate(selected)
        }
        updateItems()
    }

    fun onTableClicked(me: MouseEvent) {
        if (me.clickCount >= 2) {
            val selectedSpell = spellTable.selectionModel.selectedItem
            eventBus.publish(Event(Events.SHOW_SPELL_DETAILS, mapOf(Pair("key", selectedSpell.key))))
        }
    }
}

class SpellLevelPredicate(private val selectedLevel: SpellLevel?) : Predicate<Spell> {

    companion object {
        val ALL_LEVELS = SpellLevelPredicate(null)
    }

    override fun test(spell: Spell) = when (selectedLevel) {
        null -> true
        else -> spell.level == selectedLevel
    }
}

class SchoolPredicate(private val selectedSchool: School?) : Predicate<Spell> {

    companion object {
        val ALL_SCHOOLS = SchoolPredicate(null)
    }

    override fun test(spell: Spell) = when (selectedSchool) {
        null -> true
        else -> spell.school == selectedSchool
    }
}

class CasterPredicate(private val selectedCaster: Caster?) : Predicate<Spell> {

    companion object {
        val ALL_CASTERS = CasterPredicate(null)
    }

    override fun test(spell: Spell) = when (selectedCaster) {
        null -> true
        else -> spell.casters!!.contains(selectedCaster)
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