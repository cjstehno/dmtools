package com.stehno.dmt.spellbooks.event

import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class EventBus {

    private val subscribers = mutableMapOf<Class<Event>, MutableList<(e: Event) -> Unit>>()
    private val executorService = Executors.newFixedThreadPool(1)
    private val lock = ReentrantReadWriteLock()

    fun publish(event: Event) {
        listSubscribers(event.javaClass)?.forEach { it.invoke(event) }
    }

    fun publishAsync(event: Event) {
        listSubscribers(event.javaClass)?.forEach { op ->
            executorService.submit { op.invoke(event) }
        }
    }

    fun subscribe(eventType: Class<Event>, op: (e: Event) -> Unit) {
        lock.write {
            subscribers.computeIfAbsent(eventType) { mutableListOf() }.add(op)
        }
    }

    private fun listSubscribers(eventType: Class<Event>): List<(e: Event) -> Unit>? {
        return lock.read {
            subscribers[eventType]?.toList()
        }
    }
}