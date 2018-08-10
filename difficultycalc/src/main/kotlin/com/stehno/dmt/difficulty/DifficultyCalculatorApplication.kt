package com.stehno.dmt.difficulty

import com.stehno.dmt.difficulty.config.Context
import com.stehno.dmt.difficulty.config.ViewResolver
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class DifficultyCalculatorApplication : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(DifficultyCalculatorApplication::class.java, *args)
        }
    }

    override fun start(stage: Stage) {
        val context = Context()

        val scene = Scene(context.resolveFor(ViewResolver::class.java).resolve("/ui/main.fxml"))

        stage.title = "Difficulty Calculator"
        stage.scene = scene
        stage.isResizable = false
        stage.icons.add(Image(javaClass.getResource("/img/battle-gear.png").toString()))
        stage.show()
    }
}

