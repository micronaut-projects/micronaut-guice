package io.micronaut.guice.doc.examples.bindings.imported

import com.google.inject.AbstractModule

class CreditCardProcessorModule : AbstractModule() {
    override fun configure() {
        bind(CreditCardProcessor::class.java).to(PayPalCreditCardProcessor::class.java)
    }
}
