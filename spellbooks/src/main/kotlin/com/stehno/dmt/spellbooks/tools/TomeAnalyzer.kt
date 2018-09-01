package com.stehno.dmt.spellbooks.tools

import com.stehno.dmt.spellbooks.dsl.SpellbookLoader
import java.io.File

class TomeAnalyzer {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val dir = File(args[0])
            println("Analyzing tomes in $dir...")

            var total = 0

            dir.listFiles { d, name -> name.endsWith(".tome") }.forEach { file ->
                println("Analyzing $file")

                val spellbook = SpellbookLoader.load(file).get()
                total += spellbook.spells.size
                println("\t${spellbook.title} (${spellbook.spells.size} spells)")
            }

            println("Done ($total spells).")
        }
    }
}