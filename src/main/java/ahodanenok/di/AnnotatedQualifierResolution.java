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
    public <T extends Annotation> T resolve(Class<?> clazz) {
        return resolveImpl(Arrays.stream(clazz.getDeclaredAnnotations()));
    }

    @Override
    public <T extends Annotation> T resolve(Field field) {
        return resolveImpl(Arrays.stream(field.getDeclaredAnnotations()));
    }

    @Override
    public <T extends Annotation> T resolve(Executable executable) {
        return resolveImpl(Arrays.stream(executable.getDeclaredAnnotations()));
    }

    @Override
    public <T extends Annotation> T resolve(Executable executable, int paramNum) {
        return resolveImpl(Arrays.stream(executable.getParameterAnnotations()[paramNum]));
    }

    private <T extends Annotation> T resolveImpl(Stream<Annotation> annotations) {
        Set<Annotation> qualifiers = annotations
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toSet());

        // todo: errors

        if (qualifiers.size() > 1) {
            // todo: allow > 1?
            throw new RuntimeException("more than 1 qualifier");
        }

        if (qualifiers.size() == 1) {
            return (T) qualifiers.iterator().next();
        } else {
            return null;
        }
    }
}
