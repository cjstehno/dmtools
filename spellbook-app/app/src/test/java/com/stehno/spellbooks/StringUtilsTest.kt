package com.stehno.spellbooks

import com.stehno.spellbooks.StringUtils.extract
import org.junit.Assert.assertEquals
import org.junit.Test

internal class StringUtilsTest {

    @Test
    fun `extracting field alone`() {
        val (filter, value) = extract("school:necromancy", "school")
        assertEquals("", filter)
        assertEquals("necromancy", value)
    }

    @Test
    fun `extracting field with text`() {
        val (filter, value) = extract("school:necromancy fire", "school")
        assertEquals(" fire", filter)
        assertEquals("necromancy", value)
    }
}