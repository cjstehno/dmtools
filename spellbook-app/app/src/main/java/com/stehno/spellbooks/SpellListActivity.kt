package com.stehno.spellbooks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_spell_list.*

class SpellListActivity : AppCompatActivity() {

    private lateinit var adapter: SpellRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spell_list)

        // FIXME: temp data
        val spells = listOf(
            Spell("Transmute Test Failures into Success"),
            Spell("Fireball"),
            Spell("Dorcus' Wall of Displeasure"),
            Spell("Ambient Noise"),
            Spell("Mordeks Endless Hall"),
            Spell("Lightning Bolt"),
            Spell("Burning Hands"),
            Spell("Horrible Tickling Death"),
            Spell("Arvin's Annoying Voice"),
            Spell("Tasha's Hideous Uncontrollable Laughter"),
            Spell("Aid"),
            Spell("Cure Wounds"),
            Spell("Cat's Grace"),
            Spell("Bear's Strength"),
            Spell("Protection from Good and Evil"),
            Spell("Create Water")
        )

        adapter = SpellRecycleAdapter(this, spells) { spell ->
            Log.d("SPELL-LIST", "Clicked on $spell")
        }
        spellList.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        spellList.layoutManager = layoutManager
        spellList.setHasFixedSize(true)
    }
}
