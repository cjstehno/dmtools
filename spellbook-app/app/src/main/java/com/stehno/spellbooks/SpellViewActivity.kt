package com.stehno.spellbooks

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.stehno.spellbooks.StringUtils.renderTemplate
import kotlinx.android.synthetic.main.activity_spell_view.*
import android.text.Html



class SpellViewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SPELL = "spell"
    }

    private lateinit var spell: Spell

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spell_view)
        setSupportActionBar(viewToolbar)

        spell = intent.getParcelableExtra(EXTRA_SPELL)

        spellView.loadData(renderSpellHtml(), "text/html", "UTF-8")
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)

        menu.getItem(0).setOnMenuItemClickListener {
            sendSpell()
            false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun renderSpellHtml() = renderTemplate(resources.openRawResource(R.raw.spell_display).bufferedReader().use { it.readText() }, mapOf(
        "spell_name" to spell.name,
        "spell_level" to when (spell.level) {
            0 -> "Cantrip"
            else -> "${spell.level}-level"
        },
        "spell_school" to spell.school,
        "spell_ritual" to when (spell.ritual) {
            true -> " (ritual)"
            else -> ""
        },
        "spell_casting_time" to spell.castingTime,
        "spell_range" to spell.range,
        "spell_components" to spell.components,
        "spell_duration" to spell.duration,
        "spell_casters" to spell.casters.joinToString(", "),
        "spell_book" to spell.book,
        "spell_description" to spell.description
    ))

    private fun sendSpell() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, Html.fromHtml(renderSpellHtml(), Html.FROM_HTML_MODE_COMPACT))
            type = "text/html"
        }
        startActivity(Intent.createChooser(sendIntent, "Send to..."))
    }
}
