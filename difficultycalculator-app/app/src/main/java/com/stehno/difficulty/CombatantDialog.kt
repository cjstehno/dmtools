package com.stehno.difficulty

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.widget.EditText

class CombatantDialog : DialogFragment() {

    var combatant: Combatant? = null
    var additive = true

    private lateinit var listener: CombatantDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Combatant")

        val inflater: LayoutInflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.combatant_dialog, null)
        builder.setView(view)

        if (combatant != null) {
            val crEditor = view.findViewById<EditText>(R.id.combatant_cr)
            crEditor.text = SpannableStringBuilder(combatant!!.cr)

            val countEditor = view.findViewById<EditText>(R.id.combatant_count)
            countEditor.text = SpannableStringBuilder(combatant!!.count.toString())
        }

        builder.setNegativeButton("Cancel") { di: DialogInterface, i: Int ->
            // do nothing?
        }

        builder.setPositiveButton("OK") { di: DialogInterface, i: Int ->
            val crEditor = view.findViewById<EditText>(R.id.combatant_cr)
            val countEditor = view.findViewById<EditText>(R.id.combatant_count)
            val count: String = countEditor.text.toString()

            listener.onOkClick(Combatant(crEditor.text.toString(), count.toInt()), additive)
        }

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listener = activity as CombatantDialogListener
    }

    interface CombatantDialogListener {

        fun onOkClick(combatant: Combatant, additive: Boolean)
    }
}