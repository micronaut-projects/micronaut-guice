package io.micronaut.guice.doc.examples.bindings.annotations

// tag::class[]
import com.google.inject.AbstractModule
import com.google.inject.Provides

final class CreditCardProcessorModule extends AbstractModule {
    @Override
    protected void configure() {
        // This uses the optional `annotatedWith` clause in the `bind()` statement
        bind(CreditCardProcessor)
            .annotatedWith(PayPal)
            .to(PayPalCreditCardProcessor)
    }

    // This uses binding annotation with a @Provides method
    @Provides
    @GoogleCheckout
    CreditCardProcessor provideCheckoutProcessor(
        CheckoutCreditCardProcessor processor) {
        return processor
    }
}
// end::class[]
