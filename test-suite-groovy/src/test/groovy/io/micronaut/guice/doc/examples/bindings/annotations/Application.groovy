package io.micronaut.guice.doc.examples.bindings.annotations

// tag::imports[]
import io.micronaut.guice.annotation.Guice
import io.micronaut.runtime.Micronaut
// end::imports[]

// tag::class[]
@Guice(modules = CreditCardProcessorModule) // <1>
class Application {

    static void main(String[] args) {
        Micronaut.run(Application, args)
    }
}
// end::class[]
