/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guice.annotation;

import com.google.inject.Module;
import io.micronaut.context.annotation.AliasFor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be applied to the application entry point
 * that allows the import of Guice modules.
 *
 * <p>Micronaut will import the modules and run them at startup when the application starts
 * registering the provided beans using the Guice DSL.</p>
 *
 * <p>Note all features of Guice are supported, there exist the following limitations:</p>
 *
 * <ol>
 *     <li>Guice Scopes are not supported</li>
 *     <li>Guice AOP/Interceptors are not supported</li>
 *     <li>Guice private modules are not supported</li>
 *     <li>Static Injection is not supported</li>
 *     <li>Guice TypeConverters are not supported (use {@link io.micronaut.core.convert.TypeConverter} instead.</li>
 *     <li>Guice Listeners are not supported (use {@link io.micronaut.context.event.BeanCreatedEventListener} instead.</li>
 *     <li>None of the {@code com.google.inject.spi} API is supported</li>
 * </ol>
 *
 * <p>Note that if you create a runtime binding to a class with {@link com.google.inject.binder.LinkedBindingBuilder#to(Class)} that has no injection annotations you may need to import the bean first
 * to allow the bean to be instantiated without reflection. This can be done with {@link io.micronaut.context.annotation.Import}</p>
 *
 * <p>Otherwise it is recommended to as a minimum use the {@link jakarta.inject.Inject} annotation on the constructor to avoid this need.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Guice {
    /**
     * Import the given Guice modules.
     *
     * <p>The modules are imported in the order defined by the array.</p>
     *
     * @return An array of module types
     */
    Class<? extends Module>[] modules();

    /**
     * Import the given Guice classes.
     *
     * @return An array of classes to import
     */
    Class<?>[] classes() default {};

    /**
     * Import the given named Guice classes.
     *
     * @return An array of class names to import
     */
    @AliasFor(member = "classes")
    String[] classNames() default {};

    /**
     * The environment where the modules should be active (Defaults to all environments).
     *
     * @return The environments.
     */
    String[] environments() default {};
}
