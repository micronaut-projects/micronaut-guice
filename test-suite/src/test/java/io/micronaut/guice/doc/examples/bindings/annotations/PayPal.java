package io.micronaut.guice.doc.examples.bindings.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Qualifier
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface PayPal {}
