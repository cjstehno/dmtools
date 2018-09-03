package com.stehno.spellbooks

import java.io.DataInputStream
import java.io.InputStream

object CodexImporter {

    fun importSpells(input: InputStream, dao: SpellDao) {
        val spells = mutableListOf<Spell>()

        DataInputStream(input).use { instr ->
            val count = instr.readShort()

            (0..(count - 1)).forEach {
                spells.add(readSpell(instr))
            }
        }

        // clear out the database and add the new spells
        dao.deleteAll()
        spells.forEach { spell ->
            dao.add(spell)
        }
    }

    private fun readSpell(input: DataInputStream): Spell {
        input.readUTF() // not used by need to read
        val book = input.readUTF()
        input.readBoolean() // not used by need to read
        val name = input.readUTF()
        val ritual = input.readBoolean()
        val level = input.readInt()
        val school = input.readUTF()
        val castingTime = input.readUTF()
        val duration = input.readUTF()
        val range = input.readUTF()
        val components = input.readUTF()
        val casters = input.readUTF().split(",").toTypedArray()
        val description = input.readUTF()

        return Spell(
            name, level, school, ritual, castingTime, range, components,
            duration, casters, book, description
        )
    }
}