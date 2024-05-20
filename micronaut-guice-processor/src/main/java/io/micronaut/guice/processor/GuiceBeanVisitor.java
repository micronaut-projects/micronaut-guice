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

import com.google.inject.RestrictedBindingSource;
import io.micronaut.context.annotation.Bean;
import io.micronaut.core.annotation.AnnotationClassValue;
import io.micronaut.guice.annotation.internal.GuiceAnnotation;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.ConstructorElement;
import io.micronaut.inject.processing.ProcessingException;
import io.micronaut.inject.visitor.TypeElementVisitor;
import io.micronaut.inject.visitor.VisitorContext;
import java.util.Set;

/**
 * Guice beans have a only 1 binding type. This visitor resolves that.
 */
public class GuiceBeanVisitor
    implements TypeElementVisitor<Object, Object> {
    @Override
    public VisitorKind getVisitorKind() {
        return VisitorKind.ISOLATING;
    }

    @Override
    public Set<String> getSupportedAnnotationNames() {
        return Set.of(GuiceAnnotation.class.getName(), "com.google.inject.*");
    }

    @Override
    public void visitConstructor(ConstructorElement element, VisitorContext context) {
    }

    @Override
    public void visitClass(ClassElement element, VisitorContext context) {
        if (element.hasDeclaredAnnotation(RestrictedBindingSource.class)) {
            throw new ProcessingException(element, "The @RestrictedBindingSource annotation is not supported");
        }
        if (element.hasStereotype(GuiceAnnotation.class)) {
            exposeOnlyType(element);
        }
    }

    private static void exposeOnlyType(ClassElement element) {
        if (!element.isPresent(Bean.class, "typed")) {
            element.annotate(Bean.class, builder ->
                builder.member("typed", new AnnotationClassValue<>(element.getName()))
            );
        }
    }
}
