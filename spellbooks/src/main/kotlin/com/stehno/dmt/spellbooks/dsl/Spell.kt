package com.stehno.dmt.spellbooks.dsl

import groovy.lang.Closure
import groovy.lang.DelegatesTo
import org.dizitart.no2.IndexType
import org.dizitart.no2.objects.Id
import org.dizitart.no2.objects.Index
import org.dizitart.no2.objects.Indices
import java.io.Serializable

@Indices(value = arrayOf(
    Index(value = "key", type = IndexType.Unique),
    Index(value = "name", type = IndexType.NonUnique)
))
data class Spell(
    @Id var key: String? = null,
    var book: String? = null,
    var name: String? = null,
    var ritual: Boolean? = false,
    var level: SpellLevel? = null,
    var casters: MutableSet<Caster>? = mutableSetOf(),
    var school: School? = null,
    var castingTime: String? = null,
    var range: String? = null,
    var components: String? = null,
    var duration: String? = null,
    var description: String? = null
) : Serializable {

    // TODO: some sort of validation would be helpful

    companion object {
        @JvmStatic
        fun spell(bookTitle: String, @DelegatesTo(Spell::class) closure: Closure<Void>): Spell {
            val spell = Spell(bookTitle)
            closure.delegate = spell
            closure.call()

            spell.key = "${spell.book}:${spell.name}"

            return spell
        }
    }

    fun name(value: String) {
        name = value
    }

    fun ritual(value: Boolean? = true) {
        ritual = value
    }

    fun level(value: Int) {
        level(SpellLevel.from(value))
    }

    fun level(value: SpellLevel) {
        this.level = value
    }

    fun caster(value: Caster) {
        if (casters == null) {
            casters = mutableSetOf()
        }
        casters!!.add(value)
    }

    fun casters(vararg values: Caster) {
        if (casters == null) {
            casters = mutableSetOf()
        }
        casters!!.addAll(values)
    }

    fun school(value: School) {
        this.school = value
    }

    fun castingTime(value: String) {
        this.castingTime = value
    }

    fun range(value: String) {
        this.range = value
    }

    fun components(value: String) {
        this.components = value
    }

    fun duration(value: String) {
        this.duration = value
    }

    fun description(value: String) {
        this.description = value.trimIndent().trim()
    }
}