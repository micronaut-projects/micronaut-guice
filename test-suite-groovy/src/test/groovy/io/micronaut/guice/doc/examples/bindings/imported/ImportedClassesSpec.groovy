package io.micronaut.guice.doc.examples.bindings.imported

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class ImportedClassesSpec extends Specification {
    @Inject CreditCardProcessor processor

    void "test imported class is bound"() {
        expect:
        processor instanceof PayPalCreditCardProcessor
    }
}
