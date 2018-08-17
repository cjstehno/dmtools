package com.stehno.dmt.spellbooks.data

import com.stehno.dmt.spellbooks.dsl.Caster
import com.stehno.dmt.spellbooks.dsl.School
import com.stehno.dmt.spellbooks.dsl.SpellLevel
import org.dizitart.no2.Document
import org.dizitart.no2.NitriteId
import org.dizitart.no2.objects.ObjectFilter
import org.dizitart.no2.objects.filters.BaseObjectFilter
import org.dizitart.no2.objects.filters.ObjectFilters
import org.dizitart.no2.store.NitriteMap

data class SpellFilter(
    val level: SpellLevel? = null,
    val school: School? = null,
    val caster: Caster? = null,
    val search: String? = null,
    val books: Collection<String>? = null,
    val guildOnly: Boolean = false
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

        if (guildOnly) {
            filters.add(GuildBookObjectFilter())
        }

        return when (filters.size) {
            0 -> ObjectFilters.ALL
            else -> ObjectFilters.and(*filters.toTypedArray())
        }
    }
}

class GuildBookObjectFilter : BaseObjectFilter() {

    override fun apply(documentMap: NitriteMap<NitriteId, Document>?): MutableSet<NitriteId> {
        return documentMap?.entrySet()?.filter { ent ->
            ent.value.get("guild") as Boolean
        }?.map { ent -> ent.key }?.toMutableSet()!!
    }
}