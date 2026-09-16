package io.micronaut.guice.doc.examples.bindings.imported

import io.micronaut.guice.annotation.Guice
import io.micronaut.runtime.Micronaut

// tag::guice[]
@Guice(
    modules = CreditCardProcessorModule,
    classes = PayPalCreditCardProcessor
)
// end::guice[]
class Application {

    static void main(String[] args) {
        Micronaut.run(Application, args)
    }
}
