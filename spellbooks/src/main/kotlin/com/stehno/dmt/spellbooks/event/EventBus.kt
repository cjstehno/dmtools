package com.stehno.dmt.spellbooks.event

import com.stehno.dmt.spellbooks.data.Events
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class EventBus {

    private val subscribers = mutableMapOf<String, MutableList<(e: Event) -> Unit>>()
    private val executorService = Executors.newFixedThreadPool(1)
    private val lock = ReentrantReadWriteLock()

    fun publish(event: Event) {
        listSubscribers(event.id)?.forEach { it.invoke(event) }
    }

    fun publishAsync(event: Event) {
        listSubscribers(event.id)?.forEach { op ->
            executorService.submit { op.invoke(event) }
        }
    }

    fun subscribe(eventId: String, op: (e: Event) -> Unit) {
        lock.write {
            subscribers.computeIfAbsent(eventId) { mutableListOf() }.add(op)
        }
    }

    fun subscribe(eventId: Events, op: (e: Event) -> Unit) {
        lock.write {
            subscribers.computeIfAbsent(eventId.id) { mutableListOf() }.add(op)
        }
    }

    private fun listSubscribers(eventId: String): List<(e: Event) -> Unit>? {
        return lock.read {
            subscribers[eventId]?.toList()
        }
    }
}