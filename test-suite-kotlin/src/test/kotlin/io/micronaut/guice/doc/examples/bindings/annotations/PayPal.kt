package io.micronaut.guice.doc.examples.bindings.annotations

import jakarta.inject.Qualifier

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PayPal
