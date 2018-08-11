package com.stehno.dmt.spellbooks.event

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import spock.lang.Specification

import java.util.concurrent.CountDownLatch

class EventBusSpec extends Specification {

    private final EventBus eventBus = new EventBus()

    def 'pub-sub (sync)'() {
        setup:
        boolean fired = false
        String payload = null
        eventBus.subscribe(TestEvent){ evt ->
            fired = true
            payload = evt.payload
        }

        when:
        eventBus.publish(new TestEvent('one'))

        then:
        fired
        payload == 'one'
    }

    def 'pub-sub (async)'(){
        setup:
        CountDownLatch latch = new CountDownLatch(1)

        String payload = null
        eventBus.subscribe(TestEvent){ evt->
            payload = evt.payload
            latch.countDown()
        }

        when:
        eventBus.publishAsync(new TestEvent('two'))

        then:
        latch.await()
        payload == 'two'
    }
}

@CompileStatic @Immutable
class TestEvent implements Event {

    String payload
}
