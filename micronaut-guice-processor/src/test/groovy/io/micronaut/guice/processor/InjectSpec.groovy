package io.micronaut.guice.processor

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec

class InjectSpec extends AbstractTypeElementSpec {

    void "test inject annotation"() {
        given:
        def definition = buildBeanDefinition('test.Test', '''
package test;
import com.google.inject.Inject;

class Test {
    @Inject String whatever;
}
''')
        expect:
        definition != null
        definition.injectedFields.size() == 1
    }
}
