package io.micronaut.guice.processor

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec

class BindingAnnotationSpec
    extends AbstractTypeElementSpec {
    void "test binding annotation"() {
        given:
        def ctx = buildContext( '''
package test;

import com.google.inject.BindingAnnotation;import com.google.inject.ImplementedBy;
import com.google.inject.Inject;import com.google.inject.Singleton;import io.micronaut.context.annotation.Bean;import java.lang.annotation.Retention;import java.lang.annotation.RetentionPolicy;

class Test {
    @Inject @One public TestInterface one;
    @Inject @Two public TestInterface two;
}
interface TestInterface {
}

@Singleton
@One
@Bean(typed = TestInterface.class)
class TestImpl implements TestInterface {

}

@Singleton
@Two
@Bean(typed = TestInterface.class)
class TestImpl2 implements TestInterface {

}

@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
@interface One {}

@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
@interface Two {}
''')
        def cls = ctx.classLoader.loadClass('test.Test')
        def bean = ctx.getBean(cls)

        expect:
        bean.one.getClass().simpleName == 'TestImpl'
        bean.two.getClass().simpleName == 'TestImpl2'
    }
}
