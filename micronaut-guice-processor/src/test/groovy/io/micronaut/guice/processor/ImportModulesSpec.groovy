package io.micronaut.guice.processor

import com.google.inject.Module
import io.micronaut.annotation.processing.test.AbstractTypeElementSpec

class ImportModulesSpec
    extends AbstractTypeElementSpec {

    void "test bind instance"() {
        given:
        def context = buildContext("test.Test", '''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import io.micronaut.guice.annotation.Guice;

class SimpleModule extends AbstractModule {
    @Override protected void configure() {
        bind(String.class).toInstance("test");
    }
}

@Guice(modules= SimpleModule.class)
class Test {
    @Inject public String foo;
}
''', true)


        expect:
        context.getBean(Module.class)
        def bean = getBean(context, 'test.Test')
        bean.foo == 'test'
    }

    void "test bind provider"() {
        given:
        def context = buildContext("test.Test", '''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import io.micronaut.guice.annotation.Guice;

class SimpleModule extends AbstractModule {
    @Override protected void configure() {
        bind(String.class).toProvider(() -> "test");
    }
}

@Guice(modules= SimpleModule.class)
class Test {
    @Inject public String foo;
}
''', true)


        expect:
        context.getBean(Module.class)
        def bean = getBean(context, 'test.Test')
        bean.foo == 'test'
    }

    void "test bind instance with annotation binding"() {
        given:
        def context = buildContext("test.Test", '''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;import com.google.inject.Inject;
import io.micronaut.guice.annotation.Guice;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class SimpleModule extends AbstractModule {
    @Override protected void configure() {
        bind(String.class).annotatedWith(One.class).toInstance("test1");
        bind(String.class).annotatedWith(Two.class).toInstance("test2");
    }
}

@Guice(modules= SimpleModule.class)
class Test {
    @Inject @One public String foo;
    @Inject @Two public String bar;
}

@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
@interface One {}

@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
@interface Two {}
''', true)


        expect:
        context.getBean(Module.class)
        def bean = getBean(context, 'test.Test')
        bean.foo == 'test1'
        bean.bar == 'test2'
    }


    void "test bind interface to impl"() {
        given:
        def context = buildContext("test.Test", '''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.micronaut.guice.annotation.Guice;

class SimpleModule extends AbstractModule {
    @Override protected void configure() {
        bind(ITest.class).to(TestImpl.class);
    }
}

interface ITest {}

@Singleton
class TestImpl implements ITest {

}

@Guice(modules= SimpleModule.class)
class Test {
    @Inject public ITest test;
}
''', true)


        expect:
        context.getBean(Module.class)
        def bean = getBean(context, 'test.Test')
        bean.test != null
        bean.test.getClass().simpleName == 'TestImpl'
    }

    void "test bind interface to impl - jakarta"() {
        given:
        def context = buildContext("test.Test", '''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import io.micronaut.guice.annotation.Guice;
import jakarta.inject.Singleton;

class SimpleModule extends AbstractModule {
    @Override protected void configure() {
        bind(ITest.class).to(TestImpl.class);
    }
}

interface ITest {}

@Singleton
class TestImpl implements ITest {

}

@Guice(modules= SimpleModule.class)
class Test {
    @Inject public ITest test;
}
''', true)


        expect:
        context.getBean(Module.class)
        def bean = getBean(context, 'test.Test')
        bean.test != null
        bean.test.getClass().simpleName == 'TestImpl'
    }

    void "test bind interface to impl - import"() {
        given:
        def context = buildContext("test.Test", '''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import io.micronaut.context.annotation.Import;
import io.micronaut.guice.annotation.Guice;

class SimpleModule extends AbstractModule {
    @Override protected void configure() {
        bind(ITest.class).to(TestImpl.class);
    }
}

interface ITest {}

class TestImpl implements ITest {

}

@Guice(modules= SimpleModule.class)
@Import(classes = TestImpl.class)
class Test {
    @Inject public ITest test1;
    @Inject public ITest test2;
}
''', true)


        expect:
        context.getBean(Module.class)
        def bean = getBean(context, 'test.Test')
        bean.test1 != null
        bean.test2.getClass().simpleName == 'TestImpl'
        bean.test1 != bean.test2
    }

    void "test bind interface to impl - import as singleton"() {
        given:
        def context = buildContext("test.Test", '''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import io.micronaut.context.annotation.Import;
import io.micronaut.guice.annotation.Guice;
import jakarta.inject.Singleton;

class SimpleModule extends AbstractModule {
    @Override protected void configure() {
        bind(ITest.class).to(TestImpl.class).in(Singleton.class);
    }
}

interface ITest {}

class TestImpl implements ITest {

}

@Guice(modules= SimpleModule.class)
@Import(classes = TestImpl.class)
class Test {
    @Inject public ITest test1;
    @Inject public ITest test2;
}
''', true)


        expect:
        context.getBean(Module.class)
        def bean = getBean(context, 'test.Test')
        bean.test1 != null
        bean.test2.getClass().simpleName == 'TestImpl'
        bean.test1 == bean.test2
    }

    void "test bind interface to impl - import as singleton 2"() {
        given:
        def context = buildContext("test.Test", '''
package test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.micronaut.context.annotation.Import;
import io.micronaut.guice.annotation.Guice;

class SimpleModule extends AbstractModule {
    @Override protected void configure() {
        bind(ITest.class).to(TestImpl.class).in(Singleton.class);
    }
}

interface ITest {}

class TestImpl implements ITest {

}

@Guice(modules= SimpleModule.class)
@Import(classes = TestImpl.class)
class Test {
    @Inject public ITest test1;
    @Inject public ITest test2;
}
''', true)


        expect:
        context.getBean(Module.class)
        def bean = getBean(context, 'test.Test')
        bean.test1 != null
        bean.test2.getClass().simpleName == 'TestImpl'
        bean.test1 == bean.test2
    }
}
