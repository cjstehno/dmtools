package com.stehno.dmt.spellbooks.dsl

import groovy.lang.Closure
import groovy.lang.DelegatesTo
import groovy.lang.Script

abstract class SpellbookScript : Script() {

    fun tome(value: String) {
        spellbook().title = value
    }

    fun guild(value: Boolean = true){
        spellbook().guild = value
    }

    fun spell(@DelegatesTo(Spell::class) details: Closure<Void>) {
        spellbook().spells.add(Spell.spell(spellbook().title!!, spellbook().guild, details))
    }

    private fun spellbook() = binding.getProperty("spellbook") as Spellbook
}
