package fr.m2dl.todo.supercolorpickerultimate1.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import fr.m2dl.todo.supercolorpickerultimate1.R

class PlayerNameDialog: DialogFragment() {

    var onPlayerNameEntered: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity!!.let {
            AlertDialog.Builder(it).run {
                val playerNameEditText = EditText(context)
                setTitle("Enter player name")
                setView(playerNameEditText)

                setPositiveButton(R.string.ok) { dialog, id ->
                    val playerName = playerNameEditText.text.toString()
                    if (playerName.isNotBlank()) {
                        onPlayerNameEntered?.let { it(playerName) }
                        dialog.dismiss()
                    }
                }

                setNegativeButton(R.string.cancel) { dialog, id ->
                    dialog.cancel()
                }

                create()
            }
        }
    }
}
