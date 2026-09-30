package io.micronaut.guice.doc.examples.bindings.annotations

import com.google.inject.BindingAnnotation

@BindingAnnotation
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleCheckout
