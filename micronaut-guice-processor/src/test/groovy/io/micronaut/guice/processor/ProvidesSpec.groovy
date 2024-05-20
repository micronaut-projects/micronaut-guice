package io.micronaut.guice.processor

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec

class ProvidesSpec extends AbstractTypeElementSpec {

    void "test produces annotation"() {
        given:
        def ctx = buildContext('''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import io.micronaut.guice.annotation.Guice;

class SimpleModule extends AbstractModule {
    @Provides
    public String test() {
        return "good";
    }
}

@Guice(modules= SimpleModule.class)
class Test {
    @Inject public String foo;
}
''')
        expect:
        getBean(ctx, 'test.Test').foo == 'good'
    }

    void "test produces with generics annotation"() {
        given:
        def ctx = buildContext('''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import io.micronaut.guice.annotation.Guice;import java.util.function.Supplier;

class SimpleModule extends AbstractModule {
    @Provides
    public Supplier<String> test() {
        return () -> "good";
    }
}

@Guice(modules= SimpleModule.class)
class Test {
    @Inject public Supplier<String> foo;
}
''')
        expect:
        getBean(ctx, 'test.Test').foo.get() == 'good'
    }
}
