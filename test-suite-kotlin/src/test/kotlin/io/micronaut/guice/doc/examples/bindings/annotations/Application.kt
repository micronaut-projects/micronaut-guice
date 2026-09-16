package io.micronaut.guice.doc.examples.bindings.annotations

// tag::imports[]
import io.micronaut.guice.annotation.Guice
import io.micronaut.runtime.Micronaut
// end::imports[]

// tag::class[]
@Guice(modules = [CreditCardProcessorModule::class]) // <1>
class Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Micronaut.run(Application::class.java, *args)
        }
    }
}
// end::class[]
