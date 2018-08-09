package dmt

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class DmtApplication : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(DmtApplication::class.java, *args)
        }
    }

    override fun start(stage: Stage) {
        val context = Context(stage)

        val location = DmtApplication::class.java.getResource("/main.fxml")
        val loader = FXMLLoader(location)
        loader.setControllerFactory(context::resolve)

        val root: Parent = loader.load()

        val scene = Scene(root)

        stage.scene = scene
        stage.isResizable = false
        stage.icons.add(Image(javaClass.getResource("/battle-gear.png").toString()))
        stage.show()
    }
}

