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

import com.google.inject.Singleton;
import io.micronaut.core.annotation.AnnotationUtil;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.guice.annotation.internal.GuiceAnnotation;
import io.micronaut.inject.annotation.TypedAnnotationTransformer;
import io.micronaut.inject.visitor.VisitorContext;
import java.util.List;

/**
 * Transforms {@link com.google.inject.Singleton} to {@link jakarta.inject.Singleton}.
 */
public class SingletonTransformer
    implements TypedAnnotationTransformer<Singleton> {
    @Override
    public Class<Singleton> annotationType() {
        return Singleton.class;
    }

    @Override
    public List<AnnotationValue<?>> transform(AnnotationValue<Singleton> annotation, VisitorContext visitorContext) {
        return List.of(
            AnnotationValue.builder(AnnotationUtil.SINGLETON)
                .stereotypes(annotation.getStereotypes())
                .build(),
            GuiceAnnotation.ANNOTATION_VALUE
        );
    }
}
