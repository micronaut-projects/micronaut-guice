package io.micronaut.guice.doc.examples.bindings.imported;

import com.google.inject.AbstractModule;

public final class CreditCardProcessorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CreditCardProcessor.class).to(PayPalCreditCardProcessor.class);
    }
}
