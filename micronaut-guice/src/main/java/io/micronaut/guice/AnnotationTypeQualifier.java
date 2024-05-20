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

import io.micronaut.context.Qualifier;
import io.micronaut.core.annotation.Internal;
import io.micronaut.inject.BeanType;
import io.micronaut.inject.qualifiers.FilteringQualifier;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Simple qualifier on just annotation type.
 * @param <T> The annotation type.
 */
@Internal
final class AnnotationTypeQualifier<T> extends FilteringQualifier<T> implements Qualifier<T> {
    private final Class<? extends Annotation> annotationType;

    AnnotationTypeQualifier(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public <BT extends BeanType<T>> Stream<BT> reduce(Class<T> beanType, Stream<BT> candidates) {
        return candidates.filter(c -> c.isDeclaredAnnotationPresent(annotationType));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnnotationTypeQualifier<?> that = (AnnotationTypeQualifier<?>) o;
        return Objects.equals(annotationType, that.annotationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotationType);
    }
}
