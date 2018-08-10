package com.stehno.dmt.spellbooks.dsl

enum School {

    ABJURATION, CONJURATION, DIVINATION, ENCHANTMENT, EVOCATION, ILLUSION, NECROMANCY, TRANSMUTATION

    static School from(String value) {
        def sch = values().find { v -> v.name().equalsIgnoreCase(value) }
        if (sch) {
            return sch
        }
        throw new IllegalArgumentException("$value is not a value school name")
    }
}