package io.micronaut.guice.doc.examples.bindings.order;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.guice.doc.examples.bindings.defaultimplementation.GreeterModule;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.IntStream;

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
        assertTrue(modules.stream().anyMatch(m -> m instanceof One));
        assertTrue(modules.stream().anyMatch(m -> m instanceof Two));
        assertTrue(IntStream.range(0, modules.size())
                        .filter(i -> modules.get(i) instanceof One)
                                .findFirst().getAsInt() < IntStream.range(0, modules.size())
                .filter(i -> modules.get(i) instanceof Two)
                .findFirst().getAsInt());
    }
}

class One extends AbstractModule {

}

class Two extends AbstractModule {

}
