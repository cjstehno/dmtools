package com.stehno.spellbooks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import com.stehno.spellbooks.StringUtils.extract
import kotlinx.android.synthetic.main.activity_spell_list.*

class SpellListActivity : AppCompatActivity() {

    private lateinit var adapter: SpellRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spell_list)

        val database = SpellDatabase.getInstance(applicationContext)
        val dao = database.spellsDao()

        if (intent?.action == Intent.ACTION_SEND) {
            // import the shared spell codex
            val uri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
            if (uri != null) {
                CodexImporter.importSpells(contentResolver.openInputStream(uri), dao)
            }
        }

        val spells: MutableList<Spell> = dao.listAll().toMutableList()

        searchView.setOnEditorActionListener { textView, i, keyEvent ->
            if (keyEvent == null || keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                filterSpellList(spells, dao, textView.text.toString())
            }
            false
        }

        clearButton.setOnClickListener {
            searchView.text.clear()
            filterSpellList(spells, dao, "")
        }

        adapter = SpellRecycleAdapter(this, spells) { spell ->
            val intent = Intent(this, SpellViewActivity::class.java)
            intent.putExtra(SpellViewActivity.EXTRA_SPELL, spell)
            startActivity(intent)
        }
        spellList.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        spellList.layoutManager = layoutManager
        spellList.setHasFixedSize(true)

        updateFooterLabel(spells.size, dao.count())
    }

    private fun filterSpellList(spells: MutableList<Spell>, dao: SpellDao, searched: String) {
        spells.clear()

        if (searched.isNotBlank()) {
            val (withoutLevel, level) = extract(searched, "level")
            val (withoutCaster, caster) = extract(withoutLevel, "caster")
            val (withoutSchool, school) = extract(withoutCaster, "school")

            Log.w("SPELL-LIST", "Level: |$level|, Caster: |$caster|, School: |$school|, Text: |${withoutSchool.trim()}|")

            spells.addAll(dao.filter(withoutSchool.trim(), level?.toInt(), caster, school))
        } else {
            spells.addAll(dao.listAll())
        }

        adapter.notifyDataSetChanged()
        updateFooterLabel(spells.size, dao.count())
    }

    private fun updateFooterLabel(visible: Int, total: Int) {
        listFooter.text = "Showing $visible of $total spells"
    }
}
