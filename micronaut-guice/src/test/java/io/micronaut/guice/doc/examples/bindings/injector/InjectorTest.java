package io.micronaut.guice.doc.examples.bindings.injector;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false, environments = "injector")
@Guice(modules = FooModule.class, environments = "injector")
public class InjectorTest {
    @Test
    void testInjector(Injector injector) {
        Foo foo = injector.getInstance(Foo.class);
        Assertions.assertNotNull(foo);

        Engine<V8> v8 = injector.getInstance(Key.get(new TypeLiteral<Engine<V8>>() {
        }));
        Engine<V6> v6 = injector.getInstance(Key.get(new TypeLiteral<Engine<V6>>() {
        }));

        Assertions.assertNotNull(v8);
        Assertions.assertInstanceOf(V8.class, v8.cylinder());

        Assertions.assertNotNull(v6);
        Assertions.assertInstanceOf(V6.class, v6.cylinder());
    }
}

class FooModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<Engine<V8>>() {}).toInstance(new Engine<>(new V8()));
        bind(new TypeLiteral<Engine<V6>>() {}).toInstance(new Engine<>(new V6()));
    }
}
@Singleton
class Foo {}

record Engine<T>(T cylinder) {

}

class V8 {}
class V6 {}
