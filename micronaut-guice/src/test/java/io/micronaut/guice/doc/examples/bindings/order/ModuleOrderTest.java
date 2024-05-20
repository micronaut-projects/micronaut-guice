package io.micronaut.guice.doc.examples.bindings.order;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.guice.doc.examples.bindings.defaultimplementation.GreeterModule;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false, environments = ModuleOrderTest.ENV)
@Guice(modules = { One.class, Two.class }, environments = ModuleOrderTest.ENV)
class ModuleOrderTest {
    public static final String ENV = "order";
    @Inject
    List<Module> modules;

    @Test
    void testOrder() {
        assertEquals(3, modules.size());
        assertTrue(modules.stream().anyMatch(m -> m instanceof One));
        assertTrue(modules.stream().anyMatch(m -> m instanceof Two));
        assertTrue(modules.stream().anyMatch(m -> m instanceof GreeterModule));
    }
}

class One extends AbstractModule {

}

class Two extends AbstractModule {

}
