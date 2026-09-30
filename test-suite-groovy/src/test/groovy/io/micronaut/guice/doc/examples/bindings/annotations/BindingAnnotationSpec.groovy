package io.micronaut.guice.doc.examples.bindings.annotations

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class BindingAnnotationSpec extends Specification {
    @Inject @PayPal CreditCardProcessor paypalProcessor
    @Inject @GoogleCheckout CreditCardProcessor checkoutProcessor

    void "test inject with qualifiers"() {
        expect:
        checkoutProcessor instanceof CheckoutCreditCardProcessor
        paypalProcessor instanceof PayPalCreditCardProcessor
    }
}
