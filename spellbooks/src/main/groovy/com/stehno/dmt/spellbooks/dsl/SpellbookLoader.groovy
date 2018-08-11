package com.stehno.dmt.spellbooks.dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.util.concurrent.CompletableFuture

class SpellbookLoader {

    static CompletableFuture<Spellbook> load(final File file) {
        CompletableFuture.supplyAsync {
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
}
