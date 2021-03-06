package com.stehno.dmt.spellbooks.config

import com.stehno.dmt.spellbooks.controller.*
import com.stehno.dmt.spellbooks.data.Store
import com.stehno.dmt.spellbooks.data.StoreService
import com.stehno.dmt.spellbooks.event.EventBus
import javafx.fxml.FXMLLoader
import java.io.File

class Context {

    val objects = mutableMapOf<Class<*>, Any>()

    init {
        val eventBus = EventBus()
        val store = register(Store(storageDirectory()))
        val viewResolver = register(ViewResolver(this))
        val storeService = StoreService(store, eventBus)

        register(MainController(viewResolver, storeService, eventBus))
        register(SpellListController(storeService, eventBus))
        register(ImportProgressDialogController(storeService, eventBus))
        register(SpellDisplayContainerController(eventBus, viewResolver, storeService))
        register(SpellDetailsTabController())
    }

    private fun storageDirectory(): File {
        // FIXME: allow for specification on command line
        val file = File("${System.getProperty("user.home")}/.dmtools", "spellbooks.db")
        file.parentFile.mkdirs()
        return file
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