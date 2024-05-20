package io.micronaut.guice.doc.examples.bindings.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false, environments = ModuleOrderTest.ENV)
@Guice(modules = { One.class, Two.class }, environments = ModuleOrderTest.ENV)
class ModuleOrderTest {
    public static final String ENV = "order";
    @Inject
    List<Module> modules;

    @Test
    void testOrder() {
        assertEquals(2, modules.size());
        assertInstanceOf(One.class, modules.get(0));
        assertInstanceOf(Two.class, modules.get(1));
    }
}

class One extends AbstractModule {

}

class Two extends AbstractModule {

}
