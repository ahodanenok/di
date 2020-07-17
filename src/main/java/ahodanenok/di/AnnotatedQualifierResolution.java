package ahodanenok.di;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotatedQualifierResolution implements QualifierResolution {

    @Override
    public Annotation resolve(Class<?> clazz) {
        return resolveImpl(Arrays.stream(clazz.getDeclaredAnnotations()));
    }

    @Override
    public Annotation resolve(Field field) {
        return resolveImpl(Arrays.stream(field.getDeclaredAnnotations()));
    }

    @Override
    public Annotation resolve(Executable executable) {
        return resolveImpl(Arrays.stream(executable.getDeclaredAnnotations()));
    }

    @Override
    public Annotation resolve(Executable executable, int paramNum) {
        if (paramNum < 0 || paramNum >= executable.getParameterCount()) {
            throw new IllegalArgumentException(
                    String.format("executable has %d parameter(s), given parameter index: %d", executable.getParameterCount(), paramNum));
        }

        return resolveImpl(Arrays.stream(executable.getParameterAnnotations()[paramNum]));
    }

    private Annotation resolveImpl(Stream<Annotation> annotations) {
        Set<Annotation> qualifiers = annotations
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toSet());

        // todo: errors

        if (qualifiers.size() > 1) {
            // todo: allow > 1?
            throw new RuntimeException("more than 1 qualifier");
        }

        if (qualifiers.size() == 1) {
            return qualifiers.iterator().next();
        } else {
            return null;
        }
    }
}
