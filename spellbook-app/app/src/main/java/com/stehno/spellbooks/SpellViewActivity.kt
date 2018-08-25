package com.stehno.spellbooks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_spell_view.*

class SpellViewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SPELL = "spell"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spell_view)

        val spell = intent.getParcelableExtra<Spell>(EXTRA_SPELL)

        var template = this.resources.openRawResource(R.raw.spell_display).bufferedReader().use { it.readText() }
        template = template.replace("{{spell_name}}", spell.name)
        template = template.replace("{{spell_level}}", when (spell.level) {
            0 -> "Cantrip"
            else -> "${spell.level}-level"
        })
        template = template.replace("{{spell_school}}", spell.school)
        template = template.replace("{{spell_ritual}}", when (spell.ritual) {
            true -> " (ritual)"
            else -> ""
        })
        template = template.replace("{{spell_casting_time}}", spell.castingTime)
        template = template.replace("{{spell_range}}", spell.range)
        template = template.replace("{{spell_components}}", spell.components)
        template = template.replace("{{spell_duration}}", spell.duration)
        template = template.replace("{{spell_casters}}", spell.casters.joinToString(", "))
        template = template.replace("{{spell_book}}", spell.book)
        template = template.replace("{{spell_description}}", spell.description)

        spellView.loadData(template, "text/html", "UTF-8")
    }
}
