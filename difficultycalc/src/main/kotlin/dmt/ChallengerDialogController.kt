package dmt

import javafx.scene.control.ButtonType
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Dialog
import javafx.scene.control.Spinner
import javafx.scene.image.ImageView

class ChallengerDialogController {

    lateinit var challengerDialog: Dialog<Challenger>
    lateinit var challengerCr: ChoiceBox<String>
    lateinit var challengerCount: Spinner<Int>

    fun initialize() {
        challengerDialog.graphic = ImageView(
            javaClass.getResource("/gargoyle.png").toString()
        )

        challengerDialog.setResultConverter { button ->
            when (button) {
                ButtonType.OK -> {
                    Challenger(challengerCr.value, challengerCount.value)
                }
                else -> null
            }
        }
    }

    fun populate(challenger: Challenger) {
        challengerCr.value = challenger.challenge
        challengerCount.valueFactory.value = challenger.count
    }
}