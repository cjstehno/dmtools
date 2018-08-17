package com.stehno.dmt.spellbooks.data

import com.stehno.dmt.spellbooks.dsl.Spell
import com.stehno.dmt.spellbooks.event.Event
import com.stehno.dmt.spellbooks.event.EventBus
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.FXCollections.unmodifiableObservableList
import javafx.collections.ObservableList

class StoreService(private val store: Store, private val eventBus: EventBus) {

    fun importSpell(spell: Spell) {
        // TODO: check for existing?
        store.addSpell(spell)
    }

    // FIXME: fire the event denoting that the import is done
    fun importComplete() {
        eventBus.publish(Event(Events.SPELLS_CHANGED))
    }

    fun listSpells(filters: SpellFilter = SpellFilter()): ObservableList<Spell> {
        return unmodifiableObservableList<Spell>(observableArrayList(store.listSpells(filters)))
    }

    fun fetchSpell(key: String): Spell = store.retrieve(key)

    fun count(): Int = store.count()

    fun listBooks(): Set<String> = store.listBooks()
}

enum class Events(val id: String) {
    SPELLS_CHANGED("spells-changed"),
    SHOW_SPELL_DETAILS("show-spell-details"),
    BOOK_TOGGLED("book-toggled")
}