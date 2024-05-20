package io.micronaut.guice.doc.examples.bindings.defaultimplementation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuiceTest {
    @Test
    void guiceByDefaultUsesLastDeclaration() {
        Injector injector = Guice.createInjector(new GreeterModule());
        Greeter greeter = injector.getInstance(Greeter.class);
        assertEquals("Hello", greeter.hello());
    }
}
