package io.micronaut.guice.doc.examples.bindings.imported;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class ImportedClassesTest {
    @Inject CreditCardProcessor processor;

    @Test
    void testImportedClassIsBound() {
        assertInstanceOf(PayPalCreditCardProcessor.class, processor);
    }
}
