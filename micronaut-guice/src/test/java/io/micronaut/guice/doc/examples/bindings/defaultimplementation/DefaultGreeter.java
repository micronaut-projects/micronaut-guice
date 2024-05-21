package io.micronaut.guice.doc.examples.bindings.defaultimplementation;

public class DefaultGreeter implements Greeter {
    @Override
    public String hello() {
        return "Hello";
    }
}
