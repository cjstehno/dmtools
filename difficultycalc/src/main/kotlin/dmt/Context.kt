package dmt

import javafx.fxml.FXMLLoader
import javafx.stage.Stage

class Context(private val primaryStage: Stage) {

    private val objects = mutableMapOf<Class<*>, Any>()

    init {
        val store = register(Store())
        val viewResolver = ViewResolver(this)

        register(MainController(store))
        register(ChallengeListController(store, viewResolver))
        register(ChallengerDialogController())
    }

    fun resolve(type: Class<*>): Any? {
        return objects[type]
    }

    private fun <T : Any> register(obj: T): T {
        objects[obj.javaClass] = obj
        return obj
    }
}

class ViewResolver(val context: Context) {

    inline fun <reified T> resolve(path: String): T {
        val loader = FXMLLoader(ViewResolver::class.java.getResource(path))
        loader.setControllerFactory(context::resolve)
        return loader.load()
    }

    inline fun <reified T, C> resolveAndController(path: String): Pair<T, C> {
        val loader = FXMLLoader(ViewResolver::class.java.getResource(path))
        loader.setControllerFactory(context::resolve)
        val view = loader.load<T>()
        return Pair(view, loader.getController())
    }
}