package com.stehno.jfx

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Supplier

internal class ContextTest {

    private lateinit var context: Context

    @BeforeEach fun beforeEach() {
        context = DummyContext()
    }

    @Test fun `registering and resolving (Class)`() {
        val entity = Foo("alpha")
        val alpha = context.register(entity)

        assertThat(alpha, equalTo(entity))

        assertThat(context.resolve(Foo::class.java), equalTo(entity))

        assertThat(context.resolveAny(Foo::class.java), equalTo(entity as Any))
    }

    @Test fun `registering and resolving with Factory`() {
        val a = Foo("alpha")
        val b = Foo("bravo")

        assertThat(context.register(Supplier { a }), equalTo(a))
        assertThat(context.resolve(Foo::class.java), equalTo(a))

        assertThat(context.register({ b }, "b-foo"), equalTo(b))
        assertThat(context.resolve(Foo::class.java, "b-foo"), equalTo(b))
    }

    @Test fun `registering and resolving (String)`() {
        val a = Foo("alpha")
        val b = Foo("bravo")

        val alpha = context.register(a, "a_foo")
        val bravo = context.register(b, "b_foo")

        assertThat(alpha, equalTo(a))
        assertThat(bravo, equalTo(b))

        assertThat(context.resolve(Foo::class.java, "a_foo"), equalTo(a))
        assertThat(context.resolve(Foo::class.java, "b_foo"), equalTo(b))

        assertThat(context.resolveAny("a_foo"), equalTo(a as Any))
        assertThat(context.resolveAny("b_foo"), equalTo(b as Any))
    }

    @Test fun `loading assets`() {
        val text = context.asset("/something.txt").readText()
        assertThat(text, equalTo("Something."))
    }
}

class DummyContext : Context()

data class Foo(val value: String)