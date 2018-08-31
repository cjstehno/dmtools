package com.stehno.dmt.spellbooks.event


import spock.lang.Specification

import java.util.concurrent.CountDownLatch

class EventBusSpec extends Specification {

    private final EventBus eventBus = new EventBus()

    def 'pub-sub (sync)'() {
        setup:
        boolean fired = false
        def payload = null
        eventBus.subscribe('one') { evt ->
            fired = true
            payload = evt.payload
        }

        when:
        eventBus.publish(new Event('one', [x:42]))

        then:
        fired
        payload == [x:42]
    }

    def 'pub-sub (async)'() {
        setup:
        CountDownLatch latch = new CountDownLatch(1)

        boolean fired = false
        eventBus.subscribe('two') { evt ->
            fired = true
            latch.countDown()
        }

        when:
        eventBus.publishAsync(new Event('two', [:]))

        then:
        latch.await()
        fired
    }
}