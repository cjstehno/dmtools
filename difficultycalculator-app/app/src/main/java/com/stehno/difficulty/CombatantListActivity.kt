package com.stehno.difficulty

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_combatant_list.*

class CombatantListActivity : AppCompatActivity(), CombatantDialog.CombatantDialogListener {

    // TODO: need some visual feedback for long-press and item click
    // TODO: unit testing

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var combatantStore: CombatantStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_combatant_list)

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        combatantStore = CombatantStore()

        viewManager = LinearLayoutManager(this)
        viewAdapter = CombatantRecycleAdapter(
            combatants = combatantStore,
            itemClick = { combatant ->
                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
                Log.i(CombatantListActivity::class.java.simpleName, "Clicked item: $combatant")

                // edit item
                val dialog = CombatantDialog()
                dialog.additive = false
                dialog.combatant = combatant
                dialog.show(supportFragmentManager, "combatants")
            },
            longClick = { combatant ->
                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
                Log.i(CombatantListActivity::class.java.simpleName, "Long-press item: $combatant")

                // delete item (confirm?)
                combatantStore.remove(combatant)
                viewAdapter.notifyDataSetChanged()
            }
        )

        recyclerView = combatantList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun onAddClicked(view: View) {
        // open dialog to enter CR and Count
        val fragment = CombatantDialog()
        fragment.show(supportFragmentManager, "combatants")
    }

    fun onCalculateClicked(view: View) {
        // redirect to calculation activity
        val intent = Intent(this, DifficultyTableActivity::class.java)
        intent.putExtra(EXTRA_COMBATANTS, combatantStore.all().toTypedArray())
        startActivity(intent)
    }

    override fun onOkClick(combatant: Combatant, additive: Boolean) {
        when (additive) {
            true -> combatantStore.add(combatant)
            else -> combatantStore.update(combatant)
        }

        viewAdapter.notifyDataSetChanged()
    }
}
