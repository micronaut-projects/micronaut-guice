package io.micronaut.guice.processor

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec
import io.micronaut.inject.qualifiers.Qualifiers

class NamedSpec
    extends AbstractTypeElementSpec {

    void "test named"() {
        given:
        def definition = buildBeanDefinition('test.Test', '''
package test;

import com.google.inject.Inject;
import com.google.inject.name.Named;


class Test {
    @Named("foo")
    @Inject
    String foo;
}

''')
        expect:
        definition != null
        !definition.isSingleton()
        definition.injectedFields.size() == 1
        def qualifier = Qualifiers.forArgument(definition.injectedFields[0].asArgument())
        qualifier == Qualifiers.byName("foo")
    }
}
