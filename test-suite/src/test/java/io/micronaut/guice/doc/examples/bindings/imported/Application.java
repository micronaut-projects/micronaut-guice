package io.micronaut.guice.doc.examples.bindings.imported;

import io.micronaut.guice.annotation.Guice;
import io.micronaut.runtime.Micronaut;

// tag::guice[]
@Guice(
    modules = CreditCardProcessorModule.class,
    classes = PayPalCreditCardProcessor.class
)
// end::guice[]
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
