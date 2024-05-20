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

import com.google.inject.ImplementedBy;
import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.AnnotationClassValue;
import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.guice.annotation.internal.GuiceAnnotation;
import io.micronaut.inject.annotation.TypedAnnotationTransformer;
import io.micronaut.inject.visitor.VisitorContext;
import java.util.List;


/**
 * Transforms {@link com.google.inject.ImplementedBy} to {@link DefaultImplementation}.
 */
public class ImplementedByTransformer
    implements TypedAnnotationTransformer<ImplementedBy> {
    @Override
    public Class<ImplementedBy> annotationType() {
        return ImplementedBy.class;
    }

    @Override
    public List<AnnotationValue<?>> transform(AnnotationValue<ImplementedBy> annotation, VisitorContext visitorContext) {
        AnnotationClassValue<Object> t = annotation.stringValue().map(AnnotationClassValue::new).orElse(null);
        if (t != null) {
            return List.of(
                AnnotationValue.builder(DefaultImplementation.class)
                    .member(AnnotationMetadata.VALUE_MEMBER, t)
                    .build(),
                GuiceAnnotation.ANNOTATION_VALUE
            );
        }
        return List.of();
    }
}
