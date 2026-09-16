package io.micronaut.guice.docs.support;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Context;

import java.util.Optional;

/**
 * Forces the creation of the GraalPy runtime before the {@code @Context} {@code GuiceModuleBinder} of the
 * Guice integration runs the (Python) modules: type converters are initialized before the context-scoped beans.
 */
@Singleton
public class PythonRuntimeInitializer implements TypeConverter<Object, Object> {

    public PythonRuntimeInitializer(@Named("python") Context graalPyContext) {
        // injecting the context creates and installs the GraalPy runtime
    }

    @Override
    public Optional<Object> convert(Object object, Class<Object> targetType, ConversionContext context) {
        return Optional.empty();
    }
}
