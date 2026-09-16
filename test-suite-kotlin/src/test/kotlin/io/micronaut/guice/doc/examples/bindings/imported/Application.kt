package io.micronaut.guice.doc.examples.bindings.imported

import io.micronaut.guice.annotation.Guice
import io.micronaut.runtime.Micronaut

// tag::guice[]
@Guice(
    modules = [CreditCardProcessorModule::class],
    classes = [PayPalCreditCardProcessor::class]
)
// end::guice[]
class Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Micronaut.run(Application::class.java, *args)
        }
    }
}
