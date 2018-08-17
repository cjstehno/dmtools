package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.dsl.SpellLevel
import javafx.util.StringConverter

class SpellLevelStringConverter : StringConverter<SpellLevel>() {
    override fun fromString(lvl: String?): SpellLevel? {
        return if (lvl == "All Levels") {
            null
        } else {
            SpellLevel.from(lvl!!)
        }
    }

    override fun toString(lvl: SpellLevel?) = if (lvl != null) {
        when (lvl.level) {
            0 -> lvl.label
            else -> "${lvl.label} Level"
        }
    } else {
        "All Levels"
    }
}