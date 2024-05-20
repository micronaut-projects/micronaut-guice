package io.micronaut.guice.doc.examples.bindings.instance;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
@Guice(modules = InstanceBindingModule.class)
public class InstanceBindingTest {
    @Inject @Named("JDBC URL") String jdbcUrl;
    @Inject @Named("login timeout seconds") Integer timeout;
    @Inject @HttpPort Integer port;

    @Test
    void testInstanceBinding() {
        assertEquals("jdbc:mysql://localhost/pizza", jdbcUrl);
        assertEquals(10, timeout);
        assertEquals(8080, port);
    }
}

class InstanceBindingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(String.class)
            .annotatedWith(Names.named("JDBC URL"))
            .toInstance("jdbc:mysql://localhost/pizza");
        bind(Integer.class)
            .annotatedWith(Names.named("login timeout seconds"))
            .toInstance(10);
        bindConstant()
            .annotatedWith(HttpPort.class)
            .to(8080);
    }
}

@Qualifier
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
@interface HttpPort {}
