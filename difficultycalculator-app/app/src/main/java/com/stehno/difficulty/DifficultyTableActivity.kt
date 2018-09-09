package com.stehno.difficulty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_difficulty_table.*

class DifficultyTableActivity : AppCompatActivity() {

    // TODO: consider generating the layout programmatically


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty_table)

        val combatants = when (intent.hasExtra(EXTRA_COMBATANTS)) {
            true -> intent.getParcelableArrayExtra(EXTRA_COMBATANTS)
            else -> arrayOf()
        }.map { it as Combatant }

        Log.i(DifficultyTableActivity::class.java.simpleName, "Combatants: $combatants")

        // FIXME: calculate the difficulties
        // FIXME: apply colors for difficulty levels

        val store = CombatantStore(combatants.toMutableList())

        difficulty1.text = store.difficulty(1).toString()
        difficulty2.text = store.difficulty(2).toString()
        difficulty3.text = store.difficulty(3).toString()
        difficulty4.text = store.difficulty(4).toString()
        difficulty5.text = store.difficulty(5).toString()
        difficulty6.text = store.difficulty(6).toString()
        difficulty7.text = store.difficulty(7).toString()
        difficulty8.text = store.difficulty(8).toString()
        difficulty9.text = store.difficulty(9).toString()
        difficulty10.text = store.difficulty(10).toString()
        difficulty11.text = store.difficulty(11).toString()
        difficulty12.text = store.difficulty(12).toString()
        difficulty13.text = store.difficulty(13).toString()
        difficulty14.text = store.difficulty(14).toString()
        difficulty15.text = store.difficulty(15).toString()
        difficulty16.text = store.difficulty(16).toString()
        difficulty17.text = store.difficulty(17).toString()
        difficulty18.text = store.difficulty(18).toString()
        difficulty19.text = store.difficulty(19).toString()
        difficulty20.text = store.difficulty(20).toString()
    }
}

