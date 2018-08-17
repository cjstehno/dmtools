package com.stehno.dmt.spellbooks.data

import org.dizitart.no2.Document
import org.dizitart.no2.NitriteId
import org.dizitart.no2.objects.filters.BaseObjectFilter
import org.dizitart.no2.store.NitriteMap

class SearchStringObjectFilter(private val string: String) : BaseObjectFilter() {

    override fun apply(documentMap: NitriteMap<NitriteId, Document>?): MutableSet<NitriteId> {
        return documentMap?.entrySet()?.filter { ent ->
            ent.value.get("description").toString().contains(string, true) ||
                ent.value.get("name").toString().contains(string, true)
        }?.map { ent -> ent.key }?.toMutableSet()!!
    }
}