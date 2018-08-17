package com.stehno.dmt.spellbooks.dsl

enum class SpellLevel(val level: Int, val label: String) {

    CANTRIP(0, "Cantrip"), FIRST(1, "1st"), SECOND(2, "2nd"),
    THIRD(3, "3rd"), FOURTH(4, "4th"), FIFTH(5, "5th"),
    SIXTH(6, "6th"), SEVENTH(7, "7th"), EIGHTH(8, "8th"),
    NINTH(9, "9th");

    override fun toString() = label

    companion object {
        fun from(level: Int): SpellLevel {
            val spellLevel = values().find { it.level == level }
            when {
                spellLevel != null -> return spellLevel
                else -> throw IllegalArgumentException("The specified level ($level) is invalid.")
            }
        }

        fun from(label: String): SpellLevel {
            val spellLevel = values().find { it.label == label }
            when {
                spellLevel != null -> return spellLevel
                else -> throw IllegalArgumentException("The specified level ($label) is invalid.")
            }
        }
    }
}