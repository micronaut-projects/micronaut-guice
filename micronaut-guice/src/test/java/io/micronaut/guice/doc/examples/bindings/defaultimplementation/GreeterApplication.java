package io.micronaut.guice.doc.examples.bindings.defaultimplementation;

@io.micronaut.guice.annotation.Guice(modules = GreeterModule.class, classes = { DefaultGreeter.class, PirateGreeter.class })
public class GreeterApplication {
}
