package com.stehno.dmt.spellbooks.dsl

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode @ToString
class Spell {

    String name
    boolean ritual
    SpellLevel level
    final Set<Caster> casters = new HashSet<>()
    School school
    String castingTime
    String range
    String components
    String duration
    String description
    final String book

    Spell(final String book) {
        this.book = book
    }

    void name(final String name) {
        this.name = name
    }

    void ritual(final boolean ritual = true) {
        this.ritual = ritual
    }

    void level(final int value) {
        level(SpellLevel.from(value))
    }

    void level(final SpellLevel value) {
        this.level = value
    }

    void caster(final Caster caster) {
        this.casters.addAll(caster)
    }

    void casters(final Caster... casters) {
        this.casters.addAll(casters.collect())
    }

    void school(final School school) {
        this.school = school
    }

    void castingTime(final String value) {
        this.castingTime = value
    }

    void range(final String value) {
        this.range = value
    }

    void components(final String value) {
        this.components = value
    }

    void duration(final String value) {
        this.duration = value
    }

    void description(final String value) {
        this.description = value.stripIndent()
    }

    protected void validate() {
        // FIXME: validate the content and throw exeption
    }
}