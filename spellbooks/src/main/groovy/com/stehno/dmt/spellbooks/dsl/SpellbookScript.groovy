package com.stehno.dmt.spellbooks.dsl

abstract class SpellbookScript extends Script {

    void tome(final String name) {
        spellbook.title = name
    }

    void spell(@DelegatesTo(Spell) final Closure details) {
        Spell spell = new Spell(spellbook.title)
        details.delegate = spell
        details.call()
        spellbook.spells << spell
    }

    private Spellbook getSpellbook() {
        binding.getProperty('spellbook') as Spellbook
    }
}
