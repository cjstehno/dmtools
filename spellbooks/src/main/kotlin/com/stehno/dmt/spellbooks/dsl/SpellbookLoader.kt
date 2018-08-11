package com.stehno.dmt.spellbooks.dsl

import groovy.lang.Binding
import groovy.lang.GroovyShell
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer
import java.io.File
import java.util.concurrent.CompletableFuture

object SpellbookLoader {

    @JvmStatic
    fun load(file: File): CompletableFuture<Spellbook> {
        return CompletableFuture.supplyAsync {
            val spellbook = Spellbook()

            GroovyShell(
                SpellbookLoader::class.java.classLoader,
                Binding(mapOf(Pair("spellbook", spellbook))),
                CompilerConfiguration().apply {
                    scriptBaseClass = SpellbookScript::class.qualifiedName
                    compilationCustomizers.add(
                        ImportCustomizer().addStaticStars(Caster::class.qualifiedName, School::class.qualifiedName, SpellLevel::class.qualifiedName)
                    )
                }
            ).evaluate(file)

            spellbook
        }
    }
}
