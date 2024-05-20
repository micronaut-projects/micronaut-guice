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
package io.micronaut.guice;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.spi.Element;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.TypeConverterBinding;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.Qualifier;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.type.Argument;
import io.micronaut.inject.qualifiers.Qualifiers;
import jakarta.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Subset implementation of the {@link Injector} interface.
 */
@Singleton
@Internal
final class MicronautInjector
    implements Injector {
    private final ApplicationContext applicationContext;

    MicronautInjector(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void injectMembers(Object instance) {
        applicationContext.inject(instance);
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        return applicationContext::inject;
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
        return applicationContext::inject;
    }

    @Override
    public Map<Key<?>, Binding<?>> getBindings() {
        return Map.of();
    }

    @Override
    public Map<Key<?>, Binding<?>> getAllBindings() {
        return Map.of();
    }

    @Override
    public <T> Binding<T> getBinding(Key<T> key) {
        throw new UnsupportedOperationException("Method getBinding is not supported");
    }

    @Override
    public <T> Binding<T> getBinding(Class<T> type) {
        throw new UnsupportedOperationException("Method getBinding is not supported");
    }

    @Override
    public <T> Binding<T> getExistingBinding(Key<T> key) {
        throw new UnsupportedOperationException("Method getExistingBinding is not supported");
    }

    @Override
    public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
        return List.of();
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        Objects.requireNonNull(key, "Key cannot be null");
        Argument<T> argument = (Argument<T>) Argument.of(key.getTypeLiteral().getType());
        Qualifier<T> qualifier = toQualifier(key);
        return () -> applicationContext.getBean(argument, qualifier);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return () -> applicationContext.getBean(type);
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        Objects.requireNonNull(key, "Key cannot be null");
        Argument<T> argument = (Argument<T>) Argument.of(key.getTypeLiteral().getType());
        Qualifier<T> qualifier = toQualifier(key);
        return applicationContext.getBean(argument, qualifier);
    }

    private static <T> Qualifier<T> toQualifier(Key<T> key) {
        Class<? extends Annotation> annotationType = key.getAnnotationType();
        Qualifier<T> qualifier = null;
        Annotation annotation = key.getAnnotation();
        if (annotation instanceof Named named) {
            qualifier = Qualifiers.byName(named.value());
        } else if (annotation != null) {
            qualifier = Qualifiers.byAnnotation(annotation);
        } else if (annotationType != null) {
            qualifier = new AnnotationTypeQualifier<>(annotationType);
        }
        return qualifier;
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        return applicationContext.getBean(type);
    }

    @Override
    public Injector getParent() {
        return null;
    }

    @Override
    public Injector createChildInjector(Iterable<? extends Module> modules) {
        throw new UnsupportedOperationException("Method createChildInjector is not supported");
    }

    @Override
    public Injector createChildInjector(Module... modules) {
        throw new UnsupportedOperationException("Method createChildInjector is not supported");
    }

    @Override
    public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
        return Map.of();
    }

    @Override
    public Set<TypeConverterBinding> getTypeConverterBindings() {
        return Set.of();
    }

    @Override
    public List<Element> getElements() {
        return List.of();
    }

    @Override
    public Map<TypeLiteral<?>, List<InjectionPoint>> getAllMembersInjectorInjectionPoints() {
        return Map.of();
    }
}
