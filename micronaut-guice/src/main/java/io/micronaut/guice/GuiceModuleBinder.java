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

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.BindingAnnotation;
import com.google.inject.CreationException;
import com.google.inject.ImplementedBy;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.ProvidedBy;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.name.Named;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Message;
import com.google.inject.spi.ModuleAnnotatedMethodScanner;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeListener;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.BeanProvider;
import io.micronaut.context.RuntimeBeanDefinition;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.context.exceptions.BeanInstantiationException;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.context.exceptions.NoSuchBeanException;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.Order;
import io.micronaut.core.order.Ordered;
import io.micronaut.core.reflect.InstantiationUtils;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.StringUtils;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.annotation.MutableAnnotationMetadata;
import io.micronaut.inject.qualifiers.PrimaryQualifier;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Context
@Order(Ordered.HIGHEST_PRECEDENCE)
@Internal
class GuiceModuleBinder implements Binder {
    private static final Logger LOG = LoggerFactory.getLogger(GuiceModuleBinder.class);
    private final ApplicationContext applicationContext;
    private final List<LinkedBindingBuilderImpl<?>> linkedBindingBuilders = new ArrayList<>();
    private final List<AnnotatedConstantBindingBuilderImpl> constantBuilders = new ArrayList<>();
    private final List<Message> errors = new ArrayList<>();
    private final List<Object> toInject = new ArrayList<>();
    private Object currentSource;

    GuiceModuleBinder(
        ApplicationContext applicationContext,
        List<Module> modules) {
        this.applicationContext = applicationContext;
        for (Module module : modules) {
            withSource(module);
            module.configure(this);
        }
        try {
            if (!this.errors.isEmpty()) {
                for (Message error : errors) {
                    Throwable cause = error.getCause();
                    if (cause != null) {
                        LOG.error("Guice Module Error: " + error.getMessage(), cause);
                    } else {
                        LOG.error("Guice Module Error: {}", error.getMessage());
                    }
                }
                throw new ConfigurationException("Failed to import modules due to prior errors");
            }
            for (LinkedBindingBuilderImpl<?> builder : linkedBindingBuilders) {
                RuntimeBeanDefinition<?> beanDefinition = builder.build();
                if (beanDefinition != null) {
                    applicationContext.registerBeanDefinition(beanDefinition);
                }
            }

            for (AnnotatedConstantBindingBuilderImpl constantBuilder : constantBuilders) {
                RuntimeBeanDefinition<?> beanDefinition = constantBuilder.build();
                applicationContext.registerBeanDefinition(beanDefinition);
            }
        } finally {
            linkedBindingBuilders.clear();
            constantBuilders.clear();
        }
    }

    @EventListener
    void onStartup(StartupEvent startupEvent) {
        // run more injections
        try {
            for (Object o : toInject) {
                applicationContext.inject(o);
            }
        } finally {
            toInject.clear();
        }
    }

    @Override
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
        throw new UnsupportedOperationException("Guice interceptors are not supported");
    }

    @Override
    public void bindScope(Class<? extends Annotation> annotationType, Scope scope) {
        if (scope != Scopes.NO_SCOPE && scope != Scopes.SINGLETON) {
            throw new UnsupportedOperationException("Guice custom scopes are not supported");
        }
    }

    @Override
    public <T> LinkedBindingBuilder<T> bind(Key<T> key) {
        Argument<T> argument = (Argument<T>) Argument.of(key.getTypeLiteral().getType());
        LinkedBindingBuilderImpl<T> builder = new LinkedBindingBuilderImpl<>(argument);
        linkedBindingBuilders.add(builder);
        return builder;
    }

    @Override
    public <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
        Argument<T> argument = (Argument<T>) Argument.of(typeLiteral.getType());
        LinkedBindingBuilderImpl<T> builder = new LinkedBindingBuilderImpl<>(argument);
        linkedBindingBuilders.add(builder);
        return builder;
    }

    @Override
    public <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
        LinkedBindingBuilderImpl<T> builder = new LinkedBindingBuilderImpl<>(Argument.of(type));
        linkedBindingBuilders.add(builder);
        return builder;
    }

    @Override
    public AnnotatedConstantBindingBuilder bindConstant() {
        AnnotatedConstantBindingBuilderImpl builder = new AnnotatedConstantBindingBuilderImpl();
        constantBuilders.add(builder);
        return builder;
    }

    @Override
    public <T> void requestInjection(TypeLiteral<T> type, T instance) {
        requestInjection(instance);
    }

    @Override
    public void requestInjection(Object instance) {
        if (!toInject.contains(instance)) {
            toInject.add(instance);
        }

    }

    @Override
    public void requestStaticInjection(Class<?>... types) {
        throw new UnsupportedOperationException("Static injection is not supported");
    }

    @Override
    public void install(Module module) {
        module.configure(this);
    }

    @Override
    public Stage currentStage() {
        Set<String> activeNames = applicationContext.getEnvironment().getActiveNames();
        if (activeNames.contains(Environment.DEVELOPMENT) || activeNames.contains(Environment.TEST)) {
            return Stage.DEVELOPMENT;
        }
        return Stage.PRODUCTION;
    }

    @Override
    public void addError(String message, Object... arguments) {
        Objects.requireNonNull(message, "Message cannot be null");
        String msg = String.format(message, arguments);
        addError(new Message(msg));
    }

    @Override
    public void addError(Throwable t) {
        Objects.requireNonNull(t, "Throwable cannot be null");
        addError(new Message(t.getMessage(), t));
    }

    @Override
    public void addError(Message message) {
        Objects.requireNonNull(message, "Message cannot be null");
        errors.add(message);
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        Objects.requireNonNull(key, "Key cannot be null");
        @SuppressWarnings("unchecked")
        Argument<T> argument = (Argument<T>) Argument.of(key.getTypeLiteral().getType());
        @SuppressWarnings("unchecked")
        BeanProvider<T> provider = applicationContext.getBean(Argument.of(BeanProvider.class, argument));
        return provider::get;
    }

    @Override
    public <T> Provider<T> getProvider(Dependency<T> dependency) {
        Objects.requireNonNull(dependency, "Dependency cannot be null");
        return getProvider(dependency.getKey());
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        Objects.requireNonNull(type, "Type cannot be null");
        @SuppressWarnings("unchecked")
        BeanProvider<T> provider = applicationContext.getBean(Argument.of(BeanProvider.class, type));
        return provider::get;
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        return instance -> {
            if (!applicationContext.isRunning()) {
                throw new IllegalStateException("Injector not started");
            }
            applicationContext.inject(instance);
        };
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
        return instance -> {
            if (!applicationContext.isRunning()) {
                throw new IllegalStateException("Injector not started");
            }
            applicationContext.inject(instance);
        };
    }

    @Override
    public void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
        throw new UnsupportedOperationException("Method convertToTypes is not supported");
    }

    @Override
    public void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener) {
        throw new UnsupportedOperationException("Method bindListener is not supported");
    }

    @Override
    public void bindListener(Matcher<? super Binding<?>> bindingMatcher, ProvisionListener... listeners) {
        throw new UnsupportedOperationException("Method bindListener is not supported");
    }

    @Override
    public Binder withSource(Object source) {
        this.currentSource = source;
        return this;
    }

    @Override
    public Binder skipSources(Class<?>... classesToSkip) {
        throw new UnsupportedOperationException("Method skipSources is not supported");
    }

    @Override
    public PrivateBinder newPrivateBinder() {
        throw new UnsupportedOperationException("Private bindings are not supported");
    }

    @Override
    public void requireExplicitBindings() {
        // no-op
    }

    @Override
    public void disableCircularProxies() {
        // no-op
    }

    @Override
    public void requireAtInjectOnConstructors() {
        // no-op
    }

    @Override
    public void requireExactBindingAnnotations() {
        // no-op
    }

    @Override
    public void scanModulesForAnnotatedMethods(ModuleAnnotatedMethodScanner scanner) {
        // no-op
    }

    private static <T> void bindQualifier(RuntimeBeanDefinition.Builder<T> builder, String beanName, Class<? extends Annotation> beanQualifier, boolean primary) {
        if (StringUtils.isNotEmpty(beanName)) {
            builder.named(beanName);
        } else {
            if (beanQualifier != null) {
                MutableAnnotationMetadata annotationMetadata = new MutableAnnotationMetadata();
                annotationMetadata.addAnnotation(beanQualifier.getName(), Map.of());
                builder.annotationMetadata(annotationMetadata);
                builder.qualifier(Qualifiers.byAnnotation(annotationMetadata, beanQualifier));
            } else if (primary) {
                builder.qualifier(PrimaryQualifier.INSTANCE);
            }
        }
    }

    private static void validateBindingAnnotation(Class<? extends Annotation> annotationType) {
        Objects.requireNonNull(annotationType, "Annotation type cannot be null");
        if (annotationType.getAnnotation(BindingAnnotation.class) == null && annotationType.getAnnotation(Qualifier.class) == null) {
            throw new IllegalArgumentException("Annotation type must be annotated itself with either @BindingAnnotation or jakarta.inject.Qualifier");
        }
    }

    private static class AnnotatedConstantBindingBuilderImpl implements AnnotatedConstantBindingBuilder, ConstantBindingBuilder {
        private Object value;
        private Class<? extends Annotation> annotationType;
        private String name;

        @Override
        public ConstantBindingBuilder annotatedWith(Class<? extends Annotation> annotationType) {
            Objects.requireNonNull(annotationType, "Annotation type cannot be null");
            this.annotationType = annotationType;
            return this;
        }

        @Override
        public ConstantBindingBuilder annotatedWith(Annotation annotation) {
            Objects.requireNonNull(annotation, "Annotation cannot be null");
            if (annotation instanceof Named named) {
                this.name = named.value();
            } else {
                this.annotationType = annotation.annotationType();
            }
            return this;
        }

        @Override
        public void to(String value) {
            this.value = value;
        }

        @Override
        public void to(int value) {
            this.value = value;
        }

        @Override
        public void to(long value) {
            this.value = value;
        }

        @Override
        public void to(boolean value) {
            this.value = value;
        }

        @Override
        public void to(double value) {
            this.value = value;
        }

        @Override
        public void to(float value) {
            this.value = value;
        }

        @Override
        public void to(short value) {
            this.value = value;
        }

        @Override
        public void to(char value) {
            this.value = value;
        }

        @Override
        public void to(byte value) {
            this.value = value;
        }

        @Override
        public void to(Class<?> value) {
            this.value = value;
        }

        @Override
        public <E extends Enum<E>> void to(E value) {
            this.value = value;
        }

        public RuntimeBeanDefinition<?> build() {
            Objects.requireNonNull(value, "Binding constant cannot be null, call one of the to(..) methods on the Guice binding");
            RuntimeBeanDefinition.Builder<Object> builder = RuntimeBeanDefinition.builder(value);
            bindQualifier(builder, name, annotationType, false);
            return builder.build();
        }
    }

    private class LinkedBindingBuilderImpl<T> implements LinkedBindingBuilder<T>, AnnotatedBindingBuilder<T> {
        private final Argument<T> beanType;
        private boolean isSingleton;
        private Class<? extends Annotation> scope;

        private Supplier<T> supplier;
        private Class<? extends Annotation> annotationType;
        private String name;
        private boolean primary;

        public LinkedBindingBuilderImpl(Argument<T> argument) {
            this.beanType = argument;
        }

        @Override
        public ScopedBindingBuilder to(Class<? extends T> implementation) {
            BeanProvider<T> provider = applicationContext.getBean(Argument.of(BeanProvider.class, implementation));
            this.supplier = provider::get;
            return this;
        }

        @Override
        public ScopedBindingBuilder to(TypeLiteral<? extends T> implementation) {
            @SuppressWarnings("unchecked")
            Argument<T> argument = (Argument<T>) Argument.of(implementation.getType());
            BeanProvider<T> provider = applicationContext.getBean(Argument.of(BeanProvider.class, argument));
            this.supplier = provider::get;
            return this;
        }

        @Override
        public ScopedBindingBuilder to(Key<? extends T> targetKey) {
            @SuppressWarnings("unchecked")
            Argument<T> argument = (Argument<T>) Argument.of(targetKey.getTypeLiteral().getType());
            BeanProvider<T> provider = applicationContext.getBean(Argument.of(BeanProvider.class, argument));
            this.supplier = provider::get;
            return this;
        }

        @Override
        public void toInstance(T instance) {
            Objects.requireNonNull(instance, "Instance cannot be null");
            this.supplier = () -> instance;
        }

        @Override
        public ScopedBindingBuilder toProvider(Provider<? extends T> provider) {
            Objects.requireNonNull(provider, "Provider cannot be null");
            this.supplier = provider::get;
            return this;
        }

        @Override
        public ScopedBindingBuilder toProvider(jakarta.inject.Provider<? extends T> provider) {
            Objects.requireNonNull(provider, "Provider cannot be null");
            this.supplier = provider::get;
            return this;
        }

        @Override
        public ScopedBindingBuilder toProvider(Class<? extends jakarta.inject.Provider<? extends T>> providerType) {
            Objects.requireNonNull(providerType, "Provider type cannot be null");
            BeanProvider<jakarta.inject.Provider<T>> provider = applicationContext.getBean(Argument.of(BeanProvider.class, providerType));
            this.supplier = () -> provider.get().get();
            return this;
        }

        @Override
        public ScopedBindingBuilder toProvider(TypeLiteral<? extends jakarta.inject.Provider<? extends T>> providerType) {
            Objects.requireNonNull(providerType, "Provider type cannot be null");
            @SuppressWarnings("unchecked") Argument<? extends jakarta.inject.Provider<? extends T>> argument =
                (Argument<? extends jakarta.inject.Provider<? extends T>>) Argument.of(providerType.getType());
            BeanProvider<jakarta.inject.Provider<T>> provider = applicationContext.getBean(Argument.of(BeanProvider.class, argument));
            this.supplier = () -> provider.get().get();
            return this;
        }

        @Override
        public ScopedBindingBuilder toProvider(Key<? extends jakarta.inject.Provider<? extends T>> providerKey) {
            Objects.requireNonNull(providerKey, "Provider type cannot be null");
            return toProvider(providerKey.getTypeLiteral());
        }

        @Override
        public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor) {
            supplier = () -> InstantiationUtils.tryInstantiate(constructor)
                .orElseThrow(() -> new BeanInstantiationException("Unable to instance bean via constructor: " + constructor));
            return this;
        }

        @Override
        public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor, TypeLiteral<? extends S> type) {
            supplier = () -> InstantiationUtils.tryInstantiate(constructor)
                .orElseThrow(() -> new BeanInstantiationException("Unable to instance bean via constructor: " + constructor));
            return this;
        }

        @Override
        public void in(Class<? extends Annotation> scopeAnnotation) {
            if (scopeAnnotation == Singleton.class || scopeAnnotation == jakarta.inject.Singleton.class) {
                this.isSingleton = true;
            }
            this.scope = scopeAnnotation;
        }

        @Override
        public void in(Scope scope) {
            if (scope == Scopes.SINGLETON) {
                this.isSingleton = true;
            } else if (scope != Scopes.NO_SCOPE) {
                throw new IllegalArgumentException("Custom Guice scopes are not supported");
            }
        }

        @Override
        public void asEagerSingleton() {
            this.isSingleton = true;
            this.scope = Context.class;
        }

        public RuntimeBeanDefinition<T> build() {
            Objects.requireNonNull(beanType, "Bean type cannot be null");
            if (supplier == null) {
                // untargetted binding
                Class<T> javaType = beanType.getType();
                ImplementedBy implementedBy = javaType.getAnnotation(ImplementedBy.class);
                ProvidedBy providedBy = javaType.getAnnotation(ProvidedBy.class);
                if (implementedBy != null) {
                    if (!javaType.isAssignableFrom(implementedBy.value())) {
                        Message message = new Message(javaType, "@ImplementedBy annotation specifies a type that does not implement the declaring type");
                        throw new com.google.inject.ConfigurationException(
                            List.of(message)
                        );
                    }
                    to((Class<? extends T>) implementedBy.value());
                } else if (providedBy != null) {
                    toProvider((Class<? extends jakarta.inject.Provider<? extends T>>) providedBy.value());
                } else {
                    if (!applicationContext.containsBean(javaType)) {
                        Message message = new Message(javaType, "Cannot create untargetted binding to type that is not itself declared a bean. " +
                            "Considering adding @Guice(classes=" + javaType.getSimpleName() + ".class) below your @Guice declaration.");
                        throw new com.google.inject.ConfigurationException(
                            List.of(message)
                        );
                    } else {
                        BeanDefinition<T> beanDefinition = applicationContext.getBeanDefinition(javaType);
                        toProvider(() -> applicationContext.getBean(beanDefinition));
                        this.primary = true;
                    }
                }
            }
            Objects.requireNonNull(supplier, "Bean Provider cannot be null, call one of the binding methods like to(instance)");

            RuntimeBeanDefinition.Builder<T> builder = RuntimeBeanDefinition
                .builder(beanType, () -> {
                    try {
                        return supplier.get();
                    } catch (NoSuchBeanException e) {
                        throw new CreationException(List.of(
                            new Message("Guice binding to bean [" + beanType.getTypeName() + "] cannot be resolved since no bean exists. " +
                                "Considering adding @Guice(classes=" + beanType.getSimpleName() + ".class) to the @Guice annotation definition."),
                            new Message(e.getMessage(), e)
                        ));
                    }
                });

            if (scope != null) {
                builder.scope(scope);
            }
            if (isSingleton) {
                builder.singleton(true);
            }
            builder.exposedTypes(beanType.getType());
            String beanName = name;
            Class<? extends Annotation> beanQualifier = annotationType;
            bindQualifier(builder, beanName, beanQualifier, primary);
            return builder
                .build();
        }

        @Override
        public LinkedBindingBuilder<T> annotatedWith(Class<? extends Annotation> annotationType) {
            validateBindingAnnotation(annotationType);
            this.annotationType = annotationType;
            return this;
        }

        @Override
        public LinkedBindingBuilder<T> annotatedWith(Annotation annotation) {
            Objects.requireNonNull(annotation, "Annotation cannot be null");
            if (annotation instanceof Named named) {
                this.name = named.value();
                return this;
            } else {
                return annotatedWith(annotation.annotationType());
            }
        }
    }
}
