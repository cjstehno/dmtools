package com.stehno.dmt.spellbooks.data

import com.stehno.dmt.spellbooks.dsl.Caster
import com.stehno.dmt.spellbooks.dsl.School
import com.stehno.dmt.spellbooks.dsl.SpellLevel
import org.dizitart.no2.objects.ObjectFilter
import org.dizitart.no2.objects.filters.ObjectFilters

data class SpellFilter(
    val level: SpellLevel? = null,
    val school: School? = null,
    val caster: Caster? = null,
    val search: String? = null,
    val books: Collection<String>? = null
) {
    fun toObjectFilter(): ObjectFilter? {
        val filters = mutableListOf<ObjectFilter>()

        if (level != null) {
            filters.add(ObjectFilters.eq("level", level))
        }

        if (school != null) {
            filters.add(ObjectFilters.eq("school", school))
        }

        if (caster != null) {
            filters.add(CasterObjectFilter(caster))
        }

        if (search != null && !search.trim().isEmpty()) {
            filters.add(SearchStringObjectFilter(search))
        }

        if (books != null && !books.isEmpty()) {
            filters.add(ObjectFilters.`in`("book", *books.toTypedArray()))
        }

        return when (filters.size) {
            0 -> ObjectFilters.ALL
            else -> ObjectFilters.and(*filters.toTypedArray())
        }
    }
}