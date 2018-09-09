package com.stehno.difficulty

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CombatantRecycleAdapter(private val combatants: CombatantStore,
                              private val itemClick: (Combatant) -> Unit,
                              private val longClick: (Combatant) -> Unit) : RecyclerView.Adapter<CombatantRecycleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.combatant_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = combatants.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(combatants[position], itemClick, longClick)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(combatant: Combatant, itemClick: (Combatant) -> Unit, longClick: (Combatant) -> Unit) {
            itemView.findViewById<TextView>(R.id.combatant_item_cr).text = "CR-${combatant.cr}"
            itemView.findViewById<TextView>(R.id.combatant_item_count).text = combatant.count.toString()

            itemView.setOnClickListener { itemClick(combatant) }

            itemView.setOnLongClickListener {
                longClick(combatant)
                true
            }
        }
    }
}