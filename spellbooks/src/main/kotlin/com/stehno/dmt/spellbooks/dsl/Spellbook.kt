package com.stehno.dmt.spellbooks.dsl

class Spellbook(var title: String? = null, var guild: Boolean = false) {

    val spells = mutableSetOf<Spell>()
}
