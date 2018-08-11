package com.stehno.dmt.spellbooks.dsl

enum class SpellLevel(val level: Int) {

    CANTRIP(0), FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5), SIXTH(6), SEVENTH(7), EIGHTH(8), NINTH(9);

    override fun toString() = name.toLowerCase()

    companion object {
        fun from(level: Int): SpellLevel {
            val spellLevel = values().find { it.level == level }
            when {
                spellLevel != null -> return spellLevel
                else -> throw IllegalArgumentException("The specified level ($level) is invalid.")
            }
        }
    }
}