package io.micronaut.guice.doc.examples.bindings.imported

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class ImportedClassesTest {
    @Inject lateinit var processor: CreditCardProcessor

    @Test
    fun testImportedClassIsBound() {
        assertInstanceOf(PayPalCreditCardProcessor::class.java, processor)
    }
}
