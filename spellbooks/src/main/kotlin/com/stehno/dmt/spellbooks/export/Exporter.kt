package com.stehno.dmt.spellbooks.export

import com.stehno.dmt.spellbooks.dsl.Spell
import java.io.File

interface Exporter {

    fun export(spells: Collection<Spell>, file: File)
}