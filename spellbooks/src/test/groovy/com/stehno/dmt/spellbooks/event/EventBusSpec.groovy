package com.stehno.dmt.spellbooks.event


import spock.lang.Specification

import java.util.concurrent.CountDownLatch

class EventBusSpec extends Specification {

    private final EventBus eventBus = new EventBus()

    def 'pub-sub (sync)'() {
        setup:
        boolean fired = false
        String payload = null
        eventBus.subscribe('testing') { evt ->
            fired = true
        }

        when:
        eventBus.publish(new Event('one'))

        then:
        fired
        payload == 'one'
    }

    def 'pub-sub (async)'() {
        setup:
        CountDownLatch latch = new CountDownLatch(1)

        boolean fired = false
        eventBus.subscribe(TestEvent) { evt ->
            fired = true
            latch.countDown()
        }

        when:
        eventBus.publishAsync(new Event('two'))

        then:
        latch.await()
        fired
    }
}