package io.micronaut.guice.doc.examples.bindings.annotations2;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.guice.doc.examples.bindings.annotations2.impl.CheckoutCreditCardProcessor;
import io.micronaut.guice.doc.examples.bindings.annotations2.impl.CreditCardProcessor;
import io.micronaut.guice.doc.examples.bindings.annotations2.impl.CreditCardProcessorModule;
import io.micronaut.guice.doc.examples.bindings.annotations2.impl.GoogleCheckout;
import io.micronaut.guice.doc.examples.bindings.annotations2.impl.PayPal;
import io.micronaut.guice.doc.examples.bindings.annotations2.impl.PayPalCreditCardProcessor;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
@Guice(
    modules = CreditCardProcessorModule.class,
    packages = "io.micronaut.guice.doc.examples.bindings.annotations2.impl"
)
@Introspected(classes = PayPalCreditCardProcessor.class)
class BindingAnnotation2Test {
    @Inject @PayPal
    CreditCardProcessor paypalProcessor;
    @Inject @GoogleCheckout
    CreditCardProcessor checkoutProcessor;

    @Test
    void testInjectWithQualifiers() {
        assertInstanceOf(CheckoutCreditCardProcessor.class, checkoutProcessor);
        assertInstanceOf(PayPalCreditCardProcessor.class, paypalProcessor);
    }
}
