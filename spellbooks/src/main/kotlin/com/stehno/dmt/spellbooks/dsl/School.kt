package com.stehno.dmt.spellbooks.dsl

enum class School {

    ABJURATION, CONJURATION, DIVINATION, ENCHANTMENT, EVOCATION, ILLUSION, NECROMANCY, TRANSMUTATION;

    override fun toString() = name.toLowerCase().capitalize()

    companion object {
        fun from(value: String): School {
            val sch = values().find { v: School -> v.name.equals(value, true) }
            if (sch != null) {
                return sch
            } else {
                throw IllegalArgumentException("$value is not a value school name")
            }
        }
    }
}