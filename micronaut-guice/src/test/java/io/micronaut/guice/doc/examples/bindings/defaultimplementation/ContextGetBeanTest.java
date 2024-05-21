package io.micronaut.guice.doc.examples.bindings.defaultimplementation;

import io.micronaut.context.BeanContext;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class ContextGetBeanTest {
    @Inject
    BeanContext context;

    @Test
    void qualifiedBeanFromContextGetBean() {
        assertEquals("Ahoy", context.getBean(Greeter.class, Qualifiers.byName("pirate")).hello());

    }

    @Test
    void contextGetBean() {
        assertEquals("Hello", context.getBean(Greeter.class).hello());

    }
}
