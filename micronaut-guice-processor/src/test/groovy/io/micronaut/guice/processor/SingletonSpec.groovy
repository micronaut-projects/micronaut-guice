package io.micronaut.guice.processor

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec

class SingletonSpec
    extends AbstractTypeElementSpec {

    void "test singleton annotation"() {
        given:
        def definition = buildBeanDefinition('test.Test', '''
package test;

import com.google.inject.Singleton;

@Singleton
class Test {
}
''')
        expect:
        definition != null
        definition.isSingleton()
    }

    void "test singleton annotation has only one binding"() {
        given:
        def definition = buildBeanDefinition('test.Test', '''
package test;

import com.google.inject.Singleton;

@Singleton
class Test implements ITest {
}

interface ITest {

}
''')
        expect:
        definition != null
        definition.isSingleton()
        definition.exposedTypes == [definition.beanType] as Set
    }
}
