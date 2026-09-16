package io.micronaut.guice.doc.examples.bindings.imported

import com.google.inject.AbstractModule

final class CreditCardProcessorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CreditCardProcessor).to(PayPalCreditCardProcessor)
    }
}
