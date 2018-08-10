package com.stehno.dmt.spellbooks.dsl

import groovy.transform.TupleConstructor

@TupleConstructor
enum SpellLevel {

    CANTRIP(0), FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5), SIXTH(6), SEVENTH(7), EIGHTH(8), NINTH(9)

    final int level

    @Override String toString() {
        name().toLowerCase()
    }

    static SpellLevel from(int level) {
        values().find { it.level == level }
    }
}