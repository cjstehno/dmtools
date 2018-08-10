package com.stehno.dmt.spellbooks.dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

class SpellbookLoader {

    static Spellbook load(final File file) {
        Spellbook spellbook = new Spellbook()

        new GroovyShell(
            SpellbookLoader.classLoader,
            new Binding([
                spellbook: spellbook
            ]),
            new CompilerConfiguration(
                scriptBaseClass: SpellbookScript.name,
                compilationCustomizers: [
                    new ImportCustomizer().addStaticStars(
                        Caster.name, School.name, SpellLevel.name
                    )
                ]
            )
        ).evaluate(file)

        spellbook
    }
}
