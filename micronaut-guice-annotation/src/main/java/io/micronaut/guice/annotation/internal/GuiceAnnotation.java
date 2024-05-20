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
package io.micronaut.guice.annotation.internal;

import io.micronaut.context.annotation.Bean;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Internal meta-annotation for identifying Guice annotated beans.
 *
 * <p>A Guice annotated bean is a bean that is meta annotated with the annotation {@link com.google.inject.ScopeAnnotation}.</p>
 */
@Retention(RetentionPolicy.SOURCE)
@Internal
public @interface GuiceAnnotation {
    @NonNull AnnotationValue<GuiceAnnotation> ANNOTATION_VALUE = AnnotationValue.builder(GuiceAnnotation.class).build();
}
