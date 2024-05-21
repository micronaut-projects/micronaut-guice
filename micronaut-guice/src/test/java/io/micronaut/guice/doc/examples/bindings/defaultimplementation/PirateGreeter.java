package io.micronaut.guice.doc.examples.bindings.defaultimplementation;

public class PirateGreeter implements Greeter {
    @Override
    public String hello() {
        return "Ahoy";
    }
}
