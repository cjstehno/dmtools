package com.stehno.spellbooks

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_spell_list.*

class SpellListActivity : AppCompatActivity() {

    private lateinit var adapter: SpellRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spell_list)

        val database = SpellDatabase.getInstance(applicationContext)
        val dao = database.spellsDao()

        val spells: MutableList<Spell> = dao.listAll().toMutableList()

        searchView.setOnEditorActionListener { textView, i, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                val searched = textView.text.toString()

                Log.d("SPELL-LIST", "Searching for: $searched")

                if (searched.isNotBlank()) {
                    spells.removeAll { spell ->
                        !spell.name.contains(searched, true)
                    }

                } else {
                    spells.clear()
                    spells.addAll(dao.listAll())
                }

                adapter.notifyDataSetChanged()
            }
            false
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
    }
}
