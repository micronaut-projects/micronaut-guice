package io.micronaut.guice.doc.examples.bindings.annotations

// tag::class[]
import com.google.inject.AbstractModule
import com.google.inject.Provides

class CreditCardProcessorModule : AbstractModule() {
    override fun configure() {
        // This uses the optional `annotatedWith` clause in the `bind()` statement
        bind(CreditCardProcessor::class.java)
            .annotatedWith(PayPal::class.java)
            .to(PayPalCreditCardProcessor::class.java)
    }

    // This uses binding annotation with a @Provides method
    @Provides
    @GoogleCheckout
    fun provideCheckoutProcessor(
        processor: CheckoutCreditCardProcessor): CreditCardProcessor {
        return processor
    }
}
// end::class[]
