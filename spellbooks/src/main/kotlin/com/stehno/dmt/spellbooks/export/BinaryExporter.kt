package com.stehno.dmt.spellbooks.export

import com.stehno.dmt.spellbooks.dsl.Caster
import com.stehno.dmt.spellbooks.dsl.School
import com.stehno.dmt.spellbooks.dsl.Spell
import com.stehno.dmt.spellbooks.dsl.SpellLevel
import org.asciidoctor.Asciidoctor
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File

class BinaryExporter : Exporter {

    // TODO: needs compression

    private val asciidoctor = Asciidoctor.Factory.create()

    override fun export(spells: Collection<Spell>, file: File) {
        DataOutputStream(file.outputStream()).use { out ->
            // spell count
            out.writeShort(spells.size)

            // spells
            spells.forEach { spell ->
                writeSpell(spell, out)
            }
        }
    }

    private fun writeSpell(spell: Spell, out: DataOutputStream) {
        out.writeUTF(spell.key)
        out.writeUTF(spell.book)
        out.writeBoolean(spell.guild)
        out.writeUTF(spell.name)
        out.writeBoolean(spell.ritual!!)
        out.writeInt(spell.level!!.level)
        out.writeUTF(spell.school.toString())
        out.writeUTF(spell.castingTime)
        out.writeUTF(spell.duration)
        out.writeUTF(spell.range)
        out.writeUTF(spell.components)
        out.writeUTF(spell.casters!!.joinToString(","))
        out.writeUTF(asciidoctor.convert(spell.description, mapOf())) // TODO: note this rendering in docs
    }
}

// TODO: make this into a usable thing?
class BinaryImporter {

    fun importSpells(file: File): Collection<Spell> {
        val spells = mutableListOf<Spell>()

        DataInputStream(file.inputStream()).use { input ->
            val count = input.readShort()

            (0..(count - 1)).forEach {
                spells.add(readSpell(input))
            }
        }

        return spells
    }

    private fun readSpell(input: DataInputStream): Spell {
        return Spell(
            key = input.readUTF(),
            book = input.readUTF(),
            guild = input.readBoolean(),
            name = input.readUTF(),
            ritual = input.readBoolean(),
            level = SpellLevel.from(input.readInt()),
            school = School.from(input.readUTF()),
            castingTime = input.readUTF(),
            duration = input.readUTF(),
            range = input.readUTF(),
            components = input.readUTF(),
            casters = input.readUTF().split(",").map { Caster.from(it) }.toMutableSet(),
            description = input.readUTF()
        )
    }
}
