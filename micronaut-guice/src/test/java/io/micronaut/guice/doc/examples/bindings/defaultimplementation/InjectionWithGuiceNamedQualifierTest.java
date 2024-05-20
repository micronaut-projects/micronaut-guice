package io.micronaut.guice.doc.examples.bindings.defaultimplementation;

import com.google.inject.name.Named;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class InjectionWithGuiceNamedQualifierTest {

    @Inject
    @Named("pirate")
    Greeter greeter;

    @Test
    void qualifiedBeanFromContextGetBean() {
        assertEquals("Ahoy", greeter.hello());

    }
}
