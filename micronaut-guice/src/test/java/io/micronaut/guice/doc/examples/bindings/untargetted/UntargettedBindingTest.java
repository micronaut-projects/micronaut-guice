package io.micronaut.guice.doc.examples.bindings.untargetted;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.google.inject.AbstractModule;
import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Import;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false, environments = "untarget")
@Guice(modules = MyModule.class, environments = "untarget")
@Import(classes = MyConcreteClass.class)
public class UntargettedBindingTest {
    @Inject MyInterface myInterface1;
    @Inject MyInterface myInterface2;
    @Inject MyConcreteClass myConcreteClass;
    @Inject AnotherConcreteClass first;
    @Inject AnotherConcreteClass second;
    @Test
    void testUntargetted() {
        assertInstanceOf(MyImplementation.class, myInterface1);
        assertSame(myInterface1, myInterface2);
        assertSame(first, second);
        assertNotNull(myConcreteClass);
    }
}

class MyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MyConcreteClass.class);
        bind(MyInterface.class);
        bind(AnotherConcreteClass.class).in(Singleton.class);
    }
}

class MyConcreteClass {
}

@ImplementedBy(MyImplementation.class)
interface MyInterface {
}

@Singleton
class MyImplementation implements MyInterface {}

@Bean
class AnotherConcreteClass {}
