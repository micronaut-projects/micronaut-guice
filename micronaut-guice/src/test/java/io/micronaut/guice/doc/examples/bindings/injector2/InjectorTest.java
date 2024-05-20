package io.micronaut.guice.doc.examples.bindings.injector2;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false, environments = "generic-test")
@Guice(modules = FooModule.class, environments = "generic-test")
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

    @Provides
    public Engine<V8> v8() {
        return new Engine<>(new V8());
    }

    @Provides
    public Engine<V6> v6() {
        return new Engine<>(new V6());
    }
}
@Singleton
class Foo {}

class V8 {}
class V6 {}
