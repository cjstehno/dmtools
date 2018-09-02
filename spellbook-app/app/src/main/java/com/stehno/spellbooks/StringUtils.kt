package com.stehno.spellbooks

import android.util.Log

object StringUtils {

    fun extract(text: String, field: String): Pair<String, String?> {
        return if (text.contains("$field:")) {
            try {
                val start = text.indexOf("$field:") + field.length + 1
                val end = when (text.indexOf(" ", start)) {
                    -1 -> text.length - 1
                    else -> text.indexOf(" ", start)
                }
                val value = text.substring(start..end).trim()

                Pair(text.replace("$field:$value", ""), value)

            } catch (ex: Exception) {
                Log.e("SPELL-LIST", "Exception during $field condition resolution: ${ex.message}")
                Pair(text, null)
            }
        } else {
            Pair(text, null)
        }
    }
}