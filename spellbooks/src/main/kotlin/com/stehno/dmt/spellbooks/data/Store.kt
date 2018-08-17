package com.stehno.dmt.spellbooks.data

import com.stehno.dmt.spellbooks.dsl.Spell
import org.dizitart.kno2.nitrite
import org.dizitart.no2.Nitrite
import org.dizitart.no2.objects.filters.ObjectFilters
import java.io.File

// TODO: allow override of database location on command line

class Store(private val dbFile: File? = null) {

    private val db: Nitrite = nitrite {
        //        if (dbFile != null) {
        file = dbFile
//        }
        autoCommitBufferSize = 2048
        compress = true
        autoCompact = false
    }

    private val spellRepo = db.getRepository(Spell::class.java)

    fun addSpell(spell: Spell) {
        spellRepo.insert(spell)
    }

    fun listSpells(spellFilter: SpellFilter = SpellFilter()): List<Spell> {
        return spellRepo.find(spellFilter.toObjectFilter()).toList()
    }

    fun shutdown() {
        db.close()
    }

    fun retrieve(key: String): Spell {
        return spellRepo.find(ObjectFilters.eq("key", key)).first()
    }

    fun count(): Int {
        return spellRepo.find().size()
    }

    fun listBooks(): Set<String> {
        return spellRepo.find().map { s -> s.book!! }.toSet()
    }
}