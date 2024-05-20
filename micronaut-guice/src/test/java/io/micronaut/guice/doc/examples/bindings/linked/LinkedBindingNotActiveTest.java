package io.micronaut.guice.doc.examples.bindings.linked;

import static org.junit.jupiter.api.Assertions.assertFalse;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
public class LinkedBindingNotActiveTest {
    @Inject
    ApplicationContext applicationContext;

    @Test
    void testNotActive() {
        assertFalse(applicationContext.containsBean(BillingModule.class));
    }
}
