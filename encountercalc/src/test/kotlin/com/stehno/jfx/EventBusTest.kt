package com.stehno.jfx

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal class EventBusTest {

    @Test fun `subscribe and publish`(){
        val bus = EventBus()

        val captured = mutableListOf<Map<String,*>>()
        bus.subscribe("testing"){evt ->
            captured.add(evt.payload)
        }

        bus.publish(Event("testing", mapOf("alpha" to 42)))

        assertThat(captured.size, equalTo(1))
        assertThat(captured[0], equalTo(mapOf("alpha" to 42) as Map<String,*>))
    }

    @Test fun `subscribe and publish async`(){
        val bus = EventBus()

        val captured = mutableListOf<Map<String,*>>()
        val latch = CountDownLatch(1)
        bus.subscribe("testing"){evt ->
            captured.add(evt.payload)
            latch.countDown()
        }

        bus.publishAsync(Event("testing", mapOf("alpha" to 42)))

        latch.await(1, TimeUnit.SECONDS)

        assertThat(captured.size, equalTo(1))
        assertThat(captured[0], equalTo(mapOf("alpha" to 42) as Map<String,*>))
    }
}