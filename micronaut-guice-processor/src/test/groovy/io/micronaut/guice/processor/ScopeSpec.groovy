package io.micronaut.guice.processor

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec

class ScopeSpec
    extends AbstractTypeElementSpec {

    void "test scope annotation"() {
        given:
        def definition = buildBeanDefinition('test.Test', '''
package test;

import com.google.inject.ScopeAnnotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@MyScope
class Test {
}

@ScopeAnnotation
@Retention(RetentionPolicy.RUNTIME)
@interface MyScope {

}
''')
        expect:
        definition != null
        !definition.isSingleton()
        definition.getScopeName().get() == 'test.MyScope'
    }
}
