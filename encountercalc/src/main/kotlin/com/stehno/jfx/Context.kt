package com.stehno.jfx

import javafx.fxml.FXMLLoader
import javafx.scene.image.Image
import java.util.function.Supplier

/**
 * A lightweight dependency-injection context.
 *
 * The stored objects may be instances (by id or class name) - to provide a singleton instance of an object, or
 * as a Supplier or Supplier lambda function to provide a new instance every time the object is resolved.
 */
abstract class Context {

    val objects = mutableMapOf<String, Any>()

    /**
     * Resolves an entity in the context of the specified type, and optionally with the given id.
     *
     * @param type the entity type to be retrieved
     * @param id the optional entity id
     * @return the instance of the entity type stored in the context
     */
    inline fun <reified T> resolve(type: Class<T>, id: String? = null): T {
        val obj = objects[id ?: type.name]
        return when (obj) {
            is Function0<*> -> obj.invoke()
            is Supplier<*> -> obj.get()
            else -> obj
        } as T
    }

    fun <T : Any> register(obj: T, id: String? = null): T {
        objects[id ?: obj.javaClass.name] = obj
        return obj
    }

    /**
     * Used to register an instance factory with an optional id. The factory will be executed
     * whenever the object is resolved.
     *
     * @param factory the instance factory
     * @param id the optional id
     * @return an instance of the object supplied by the factory
     */
    fun <T : Any> register(factory: Supplier<T>, id: String? = null): T {
        val obj = factory.get()
        objects[id ?: obj.javaClass.name] = factory
        return obj
    }

    fun <T : Any> register(factory: () -> T, id: String? = null): T {
        val obj = factory()
        objects[id ?: obj.javaClass.name] = factory
        return obj
    }

    fun resolveAny(type: Class<*>): Any? {
        val obj = objects[type.name]
        return when (obj) {
            is Supplier<*> -> obj.get()
            else -> obj
        }
    }

    fun resolveAny(id: String): Any? {
        val obj = objects[id]
        return when (obj) {
            is Supplier<*> -> obj.get()
            else -> obj
        }
    }

    fun asset(path: String) = Context::class.java.getResource(path)!!

    fun image(path: String) = Image(asset(path).toString())

    inline fun <reified T> view(path: String): T {
        return loader(path).load()
    }

    inline fun <reified V, C> viewAndController(path: String): Pair<V, C> {
        val loader = loader(path)
        return Pair(loader.load(), loader.getController())
    }

    fun loader(path: String): FXMLLoader {
        val loader = FXMLLoader(asset(path))
        loader.setControllerFactory(this::resolveAny)
        return loader
    }
}