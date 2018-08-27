package com.stehno.spellbooks

import android.arch.persistence.room.TypeConverter

object CasterTypeConverters {

    @JvmStatic
    @TypeConverter
    fun castersFromString(casters: String): Array<String> = casters.split(",").toTypedArray()

    @JvmStatic
    @TypeConverter
    fun stringFromCasters(casters: Array<String>): String = casters.joinToString(",")
}