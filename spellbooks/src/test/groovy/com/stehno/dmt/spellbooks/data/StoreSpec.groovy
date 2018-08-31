package com.stehno.dmt.spellbooks.data

import com.stehno.dmt.spellbooks.dsl.Caster
import com.stehno.dmt.spellbooks.dsl.School
import com.stehno.dmt.spellbooks.dsl.Spell
import com.stehno.dmt.spellbooks.dsl.SpellLevel
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class StoreSpec extends Specification {

    @Rule TemporaryFolder folder = new TemporaryFolder()

    private Store store

    def setup() {
        store = new Store(/*folder.newFile()*/)
    }

    def 'add spells'() {
        setup:
        Spell spell = Spell.spell('Testing Magic', true){
            name 'Summon Unit Test'
            level SpellLevel.FIRST
            ritual()
            casters Caster.BARD, Caster.CLERIC
            school School.CONJURATION
            castingTime '100 ms'
            range 'Unit'
            components 'V, S, M (a computer)'
            duration 'Instantaneous'
            description 'A unit test is summoned and executed.'
        }

        when:
        store.addSpell(spell)

        List<Spell> spells = store.listSpells()

        then:
        spells.size() == 1
        spell in spells
    }

    def cleanup() {
        store?.shutdown()
    }
}
