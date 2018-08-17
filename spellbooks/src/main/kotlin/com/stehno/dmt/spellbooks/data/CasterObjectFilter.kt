package com.stehno.dmt.spellbooks.data

import com.stehno.dmt.spellbooks.dsl.Caster
import org.dizitart.no2.Document
import org.dizitart.no2.NitriteId
import org.dizitart.no2.objects.filters.BaseObjectFilter
import org.dizitart.no2.store.NitriteMap

class CasterObjectFilter(private val target: Caster) : BaseObjectFilter() {

    override fun apply(documentMap: NitriteMap<NitriteId, Document>?): MutableSet<NitriteId> {
        return documentMap?.entrySet()?.filter { ent ->
            (ent.value.get("casters") as Collection<*>).contains(target.name)
        }?.map { ent -> ent.key }?.toMutableSet()!!
    }
}