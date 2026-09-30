package io.micronaut.guice.doc.examples.bindings.annotations

import jakarta.inject.Singleton

@Singleton
class PayPalCreditCardProcessor : CreditCardProcessor
