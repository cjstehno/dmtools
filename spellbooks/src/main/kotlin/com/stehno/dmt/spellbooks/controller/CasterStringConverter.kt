package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.dsl.Caster
import javafx.util.StringConverter

class CasterStringConverter : StringConverter<Caster>() {

    override fun fromString(caster: String?): Caster? = if (caster == "All Casters") {
        null
    } else {
        Caster.from(caster!!)
    }

    override fun toString(caster: Caster?) = caster?.toString() ?: "All Casters"
}