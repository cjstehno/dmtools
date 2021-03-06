package com.stehno.dmt.difficulty.config

import com.stehno.dmt.difficulty.Store
import com.stehno.dmt.difficulty.controller.ChallengeListController
import com.stehno.dmt.difficulty.controller.ChallengerDialogController
import com.stehno.dmt.difficulty.controller.DifficultyTableController
import com.stehno.dmt.difficulty.controller.MainController
import javafx.fxml.FXMLLoader

class Context {

    val objects = mutableMapOf<Class<*>, Any>()

    init {
        val store = register(Store())
        val viewResolver = register(ViewResolver(this))

        register(MainController())
        register(ChallengeListController(store, viewResolver))
        register(ChallengerDialogController())
        register(DifficultyTableController(store))
    }

    fun resolve(type: Class<*>): Any? {
        return objects[type]
    }

    inline fun <reified T> resolveFor(type: Class<T>): T {
        return objects[type] as T
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