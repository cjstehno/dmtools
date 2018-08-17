package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.dsl.School
import javafx.util.StringConverter

class SchoolStringConverter : StringConverter<School>() {
    override fun fromString(school: String?): School? {
        return if (school == "All Schools") {
            null
        } else {
            School.from(school!!)
        }
    }

    override fun toString(school: School?) = school?.toString() ?: "All Schools"
}