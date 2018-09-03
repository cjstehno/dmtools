package com.stehno.spellbooks

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class SpellRecycleAdapter(private val context: Context,
                          private val spells: List<Spell>,
                          private val itemClick: (Spell) -> Unit) : RecyclerView.Adapter<SpellRecycleAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(spells[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.spell_list_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount() = spells.size

    inner class Holder(itemView: View?,
                       private val itemClick: (Spell) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private var spellText = itemView?.findViewById<TextView>(R.id.spellItemText)

        fun bind(spell: Spell) {
            spellText?.text = spell.name

            itemView.setOnClickListener { itemClick(spell) }
        }
    }
}