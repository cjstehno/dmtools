package com.stehno.dmt.spellbooks.dsl

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class SpellbookLoaderSpec extends Specification {

    @Rule TemporaryFolder folder = new TemporaryFolder()

    def 'loading spellbook'() {
        setup:
        File file = folder.newFile()
        file.text = '''
            tome "Zoldeck's Tome of Wonders"
            
            spell {
                name "Acid Arrow"
                level 0
                casters SORCERER, WIZARD
                school CONJURATION
                castingTime '1 action'
                range '60 feet'
                components 'V, S'
                duration 'Instantaneous'
                description """
                    You launch an arrow of acid at a single target. If the spell attack hits, the target 
                    takes 1d6 acid damage.
            
                    **At higher level.** This spell’s damage increases by 1d6 when you reach 5th level (2d6), 
                    11th level (3d6), and 17th level (4d6).
                """
            }
        '''

        when:
        Spellbook spellbook = SpellbookLoader.load(file)

        then:
        spellbook.title == 'Zoldeck\'s Tome of Wonders'
        spellbook.spells.size() == 1
        spellbook.spells[0].name == 'Acid Arrow'
        spellbook.spells[0].level == SpellLevel.CANTRIP
        spellbook.spells[0].casters.containsAll([Caster.SORCERER, Caster.WIZARD])
        spellbook.spells[0].school == School.CONJURATION
        spellbook.spells[0].castingTime == '1 action'
        spellbook.spells[0].range == '60 feet'
        spellbook.spells[0].duration == 'Instantaneous'
        spellbook.spells[0].components == 'V, S'
        spellbook.spells[0].description == '''
            You launch an arrow of acid at a single target. If the spell attack hits, the target 
            takes 1d6 acid damage.
            
            **At higher level.** This spell’s damage increases by 1d6 when you reach 5th level (2d6), 
            11th level (3d6), and 17th level (4d6).
        '''.stripIndent()
    }
}
