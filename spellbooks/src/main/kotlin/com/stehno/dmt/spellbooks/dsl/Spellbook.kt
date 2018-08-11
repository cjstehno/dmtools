package com.stehno.dmt.spellbooks.dsl

class Spellbook(var title: String?=null) {

    val spells = mutableSetOf<Spell>()
}
