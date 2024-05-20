package io.micronaut.guice.processor

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec
import io.micronaut.context.annotation.DefaultImplementation
import spock.lang.PendingFeature

class ImplementedBySpec
    extends AbstractTypeElementSpec {

    @PendingFeature(reason = "requires Micronaut 4.5 - see https://github.com/micronaut-projects/micronaut-core/pull/10820")
    void "test implemented by"() {
        given:
        def ctx = buildContext( '''
package test;

import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

@ImplementedBy(TestImpl.class)
interface Test {
}

@Singleton
class TestImpl implements Test {

}

@Singleton
class TestImpl2 implements Test {

}
''')
        def cls = ctx.classLoader.loadClass('test.Test')
        expect:
        ctx.getBean(cls).class.simpleName == 'TestImpl'
    }
}
