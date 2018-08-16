package com.stehno.dmt.spellbooks

import com.stehno.dmt.spellbooks.config.Context
import com.stehno.dmt.spellbooks.config.ViewResolver
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class SpellbooksApplication : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(SpellbooksApplication::class.java, *args)
        }
    }

    override fun start(stage: Stage) {
        val context = Context()

        val scene = Scene(context.resolveFor(ViewResolver::class.java).resolve("/ui/main.fxml"))

        stage.title = "Spellbooks"
        stage.scene = scene
        stage.isResizable = true
        stage.icons.add(Image(javaClass.getResource("/img/book-cover.png").toString()))
        stage.show()
    }
}