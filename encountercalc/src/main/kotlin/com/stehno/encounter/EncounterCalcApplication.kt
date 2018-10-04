package com.stehno.encounter

import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class EncounterCalcApplication : Application() {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            Application.launch(EncounterCalcApplication::class.java, *args)
        }
    }

    override fun start(stage: Stage) {
        val context = ApplicationContext()

        val parent = context.view<Parent>("/ui/main.fxml")
        val scene = Scene(parent)

        stage.title = "POC"
        stage.scene = scene
        stage.isResizable = true
        stage.icons.add(context.image("/img/toxic-warning-sign.png"))
        stage.show()
    }
}
