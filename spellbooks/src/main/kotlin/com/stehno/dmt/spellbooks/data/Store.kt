package com.stehno.dmt.spellbooks.data

import com.stehno.dmt.spellbooks.dsl.Spell
import org.dizitart.kno2.nitrite
import org.dizitart.no2.Nitrite
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

    fun listSpells(): List<Spell> {
        return spellRepo.find().toList()
    }

    fun shutdown() {
        db.close()
    }
}