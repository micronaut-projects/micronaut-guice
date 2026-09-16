package io.micronaut.guice.doc.examples.bindings.annotations;

// tag::imports[]
import io.micronaut.guice.annotation.Guice;
import io.micronaut.runtime.Micronaut;
// end::imports[]

// tag::class[]
@Guice(modules = CreditCardProcessorModule.class) // <1>
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
// end::class[]
