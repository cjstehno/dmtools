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

        // FIXME: temp data
        val allSpells = listOf(
            Spell("Transmute Test Failures into Success", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This <b>is</b> a spell description"),
            Spell("Fireball", 7, "Abjuration", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Dorcus' Wall of Displeasure", 0, "Necromancy", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Ambient Noise", 5, "Transmutation", false, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Mordeks Endless Hall", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Lightning Bolt", 3, "Transmutation", false, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Burning Hands", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Horrible Tickling Death", 1, "Conjuration", false, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Arvin's Annoying Voice", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Tasha's Hideous Uncontrollable Laughter", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Aid", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Cure Wounds", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Cat's Grace", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Bear's Strength", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Protection from Good and Evil", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description"),
            Spell("Create Water", 0, "Transmutation", true, "1 round", "30 feet", "V, S", "Instantaneous", arrayOf("Wizard", "Warlock"), "Tome of Wonders", "This is a spell description")
        ).sortedBy { it.name }

        val spells: MutableList<Spell> = allSpells.toMutableList()

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
                    spells.addAll(allSpells)
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
