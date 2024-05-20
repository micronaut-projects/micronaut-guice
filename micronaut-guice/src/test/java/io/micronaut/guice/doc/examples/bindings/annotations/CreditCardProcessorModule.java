package io.micronaut.guice.doc.examples.bindings.annotations;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

final class CreditCardProcessorModule extends AbstractModule {
    @Override
    protected void configure() {
        // This uses the optional `annotatedWith` clause in the `bind()` statement
        bind(CreditCardProcessor.class)
            .annotatedWith(PayPal.class)
            .to(PayPalCreditCardProcessor.class);
    }

    // This uses binding annotation with a @Provides method
    @Provides
    @GoogleCheckout
    public CreditCardProcessor provideCheckoutProcessor(
        CheckoutCreditCardProcessor processor) {
        return processor;
    }
}
