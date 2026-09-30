package io.micronaut.guice.doc.examples.bindings.annotations

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class BindingAnnotationTest {
    @Inject @PayPal lateinit var paypalProcessor: CreditCardProcessor
    @Inject @GoogleCheckout lateinit var checkoutProcessor: CreditCardProcessor

    @Test
    fun testInjectWithQualifiers() {
        assertInstanceOf(CheckoutCreditCardProcessor::class.java, checkoutProcessor)
        assertInstanceOf(PayPalCreditCardProcessor::class.java, paypalProcessor)
    }
}
