package io.micronaut.guice.doc.examples.bindings.defaultimplementation;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

public class GreeterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Greeter.class).annotatedWith(Names.named("pirate")).to(PirateGreeter.class).in(Scopes.SINGLETON);
        bind(Greeter.class).to(DefaultGreeter.class).in(Scopes.SINGLETON);
    }
}
