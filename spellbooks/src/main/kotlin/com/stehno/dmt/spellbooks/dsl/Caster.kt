package com.stehno.dmt.spellbooks.dsl

enum class Caster {
    BARD, CLERIC, DRUID, PALADIN, RANGER, SORCERER, WARLOCK, WIZARD;

    override fun toString() = name.toLowerCase().capitalize()

    companion object {
        fun from(caster: String): Caster{
            val found = values().find { c -> c.toString() == caster }
            when(found){
                null -> throw IllegalArgumentException("Invalid caster.")
                else -> return found
            }
        }
    }
}