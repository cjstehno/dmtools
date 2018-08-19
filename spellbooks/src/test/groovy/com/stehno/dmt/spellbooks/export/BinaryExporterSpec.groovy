package com.stehno.dmt.spellbooks.export

import com.stehno.dmt.spellbooks.dsl.Caster
import com.stehno.dmt.spellbooks.dsl.School
import com.stehno.dmt.spellbooks.dsl.Spell
import com.stehno.dmt.spellbooks.dsl.SpellLevel
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class BinaryExporterSpec extends Specification {

    @Rule TemporaryFolder folder = new TemporaryFolder()

    def 'exporting'() {
        setup:
        File file = folder.newFile()
        Exporter exporter = new BinaryExporter()

        BinaryImporter importer = new BinaryImporter()

        when:
        exporter.export([
            Spell.spell('Testing Spells', false) {
                name 'Unit Test'
                level SpellLevel.SECOND
                school School.ABJURATION
                castingTime '1 minute'
                duration 'Instantaneous'
                range '30 feet'
                components 'V, S'
                casters Caster.WIZARD, Caster.WARLOCK
                description 'This is a test spell.'
            },
            Spell.spell('Testing Spells', false) {
                name 'Dumpster Fire'
                level SpellLevel.THIRD
                school School.CONJURATION
                castingTime '1 minute'
                duration 'Instantaneous'
                range '30 feet'
                components 'V, S'
                casters Caster.WIZARD, Caster.WARLOCK
                description 'This is a test spell.'
            }
        ], file)

        def imported = importer.importSpells(file)

        then:
        imported.size() == 2

        imported[0].book == 'Testing Spells'
        !imported[0].guild
        imported[0].name == 'Unit Test'
        imported[0].level == SpellLevel.SECOND
        imported[0].school == School.ABJURATION
        imported[0].castingTime == '1 minute'
        imported[0].duration == 'Instantaneous'
        imported[0].range == '30 feet'
        imported[0].components == 'V, S'
        imported[0].casters.containsAll([Caster.WIZARD, Caster.WARLOCK])
        imported[0].description == 'This is a test spell.'

        imported[1].book == 'Testing Spells'
        !imported[1].guild
        imported[1].name == 'Dumpster Fire'
        imported[1].level == SpellLevel.THIRD
        imported[1].school == School.CONJURATION
        imported[1].castingTime == '1 minute'
        imported[1].duration == 'Instantaneous'
        imported[1].range == '30 feet'
        imported[1].components == 'V, S'
        imported[1].casters.containsAll([Caster.WIZARD, Caster.WARLOCK])
        imported[1].description == 'This is a test spell.'
    }
}
