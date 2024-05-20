package io.micronaut.guice.doc.examples.bindings.annotations;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import io.micronaut.context.annotation.Import;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
@Guice(modules = CreditCardProcessorModule.class)
@Import(classes = {PayPalCreditCardProcessor.class, CheckoutCreditCardProcessor.class})
class BindingAnnotationTest {
    @Inject @PayPal CreditCardProcessor paypalProcessor;
    @Inject @GoogleCheckout CreditCardProcessor checkoutProcessor;

    @Test
    void testInjectWithQualifiers() {
        assertInstanceOf(CheckoutCreditCardProcessor.class, checkoutProcessor);
        assertInstanceOf(PayPalCreditCardProcessor.class, paypalProcessor);
    }
}
