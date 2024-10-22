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
package io.micronaut.guice.processor;

import com.google.inject.Module;
import com.google.inject.Provides;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Order;
import io.micronaut.core.util.ArrayUtils;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.ElementQuery;
import io.micronaut.inject.ast.MethodElement;
import io.micronaut.inject.ast.beans.BeanElementBuilder;
import io.micronaut.inject.processing.ProcessingException;
import io.micronaut.inject.visitor.TypeElementVisitor;
import io.micronaut.inject.visitor.VisitorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ImportModuleVisitor
    implements TypeElementVisitor<Guice, Object> {

    public static final String MEMBER_ENVS = "environments";
    public static final String MEMBER_MODULES = "modules";
    public static final String MEMBER_CLASSES = "classes";
    public static final String MEMBER_PACKAGES = "packages";

    @Override
    public void visitClass(ClassElement element, VisitorContext context) {
        @NonNull String[] moduleNames = element.stringValues(Guice.class, MEMBER_MODULES);
        @NonNull String[] classNames = element.stringValues(Guice.class, MEMBER_CLASSES);
        @NonNull String[] packages = element.stringValues(Guice.class, MEMBER_PACKAGES);
        @NonNull String[] envs = element.stringValues(Guice.class, MEMBER_ENVS);
        List<ClassElement> classElements = new ArrayList<>();
        for (String className : classNames) {
            ClassElement classElement = context.getClassElement(className).orElse(null);
            if (classElement == null) {
                throw new ProcessingException(element, "Guice class import [" + className + "] must be on the compilation classpath");
            } else {
                classElements.add(classElement);
            }
        }

        if (ArrayUtils.isNotEmpty(packages)) {
            for (String aPackage : packages) {
                final ClassElement[] inPackage = context
                    .getClassElements(aPackage, "*");
                for (ClassElement classElement : inPackage) {
                    if (!classElement.isAbstract() && !classElement.isPrivate() && !classElement.isAssignable(Module.class)) {
                        classElements.add(classElement);
                    }
                }
            }
        }

        for (ClassElement classElement : classElements) {
            BeanElementBuilder builder = element.addAssociatedBean(classElement);
            builder.inject();
            builder.typed(classElement);
        }
        for (int i = 0; i < moduleNames.length; i++) {
            String className = moduleNames[i];
            ClassElement moduleElement = context.getClassElement(className).orElse(null);
            if (moduleElement == null) {
                throw new ProcessingException(element, "Guice module [" + className + "] must be on the compilation classpath");
            }
            int order = i;
            MethodElement primaryConstructor =
                moduleElement.getPrimaryConstructor().orElse(null);
            if (primaryConstructor == null) {
                throw new ProcessingException(element, """
                        Cannot import Guice module [" + moduleElement.getName() + "], since it has multiple constructors or no accessible constructor.
                        Consider defining a single public accessible constructor or if there are multiple adding @Inject to one of them.
                    """);
            } else {

                BeanElementBuilder beanElementBuilder = element.addAssociatedBean(
                    moduleElement
                ).annotate(Order.class, builder ->
                    builder.value(order) // retain load order
                );
                if (ArrayUtils.isNotEmpty(envs)) {
                    beanElementBuilder.annotate(Requires.class, env -> env.member("env", envs));
                }
                beanElementBuilder.createWith(primaryConstructor);
                ElementQuery<MethodElement> producesMethodQuery = ElementQuery.ALL_METHODS
                    .annotated(am -> am.hasAnnotation(Provides.class))
                    .onlyDeclared()
                    .onlyConcrete();
                List<MethodElement> methodElements = moduleElement.getEnclosedElements(producesMethodQuery);
                for (MethodElement methodElement : methodElements) {
                    if (!methodElement.isPublic()) {
                        throw new ProcessingException(methodElement, "Method's annotated with @Produces must be public");
                    }
                    if (methodElement.getReturnType().isVoid()) {
                        throw new ProcessingException(methodElement, "Method's annotated with @Produces cannot return 'void'");
                    }
                    if (!methodElement.getReturnType().isPublic()) {
                        throw new ProcessingException(methodElement, "Method's annotated with @Produces must return a publicly accessible type");
                    }
                }
                beanElementBuilder.produceBeans(producesMethodQuery, childBuilder -> {
                    MethodElement methodElement = (MethodElement) childBuilder.getProducingElement();
                    ClassElement genericReturnType = methodElement.getGenericReturnType();
                    childBuilder.typed(genericReturnType);
                    ClassElement[] typeArguments = genericReturnType.getBoundGenericTypes().toArray(ClassElement[]::new);
                    childBuilder.typeArguments(typeArguments);
                    childBuilder.annotate(Primary.class);
                    AnnotationMetadata annotationMetadata = methodElement.getAnnotationMetadata();
                    Set<String> annotationNames = annotationMetadata.getAnnotationNames();
                    for (String annotationName : annotationNames) {
                        if (!annotationName.equals(Provides.class.getName())) {
                            annotationMetadata.findAnnotation(annotationName)
                                .ifPresent(childBuilder::annotate);
                        }
                    }
                    if (ArrayUtils.isNotEmpty(envs)) {
                        childBuilder.annotate(Requires.class, env -> env.member("env", envs));
                    }
                });

                beanElementBuilder.typed(ClassElement.of(Module.class), moduleElement);
            }
        }
    }

    @Override
    public VisitorKind getVisitorKind() {
        return VisitorKind.ISOLATING;
    }

    @Override
    public Set<String> getSupportedAnnotationNames() {
        return Set.of(Guice.class.getName());
    }
}
