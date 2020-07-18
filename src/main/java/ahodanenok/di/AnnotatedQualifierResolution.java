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
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        return resolveImpl(Arrays.stream(clazz.getDeclaredAnnotations()));
    }

    @Override
    public Annotation resolve(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("field is null");
        }

        return resolveImpl(Arrays.stream(field.getDeclaredAnnotations()));
    }

    @Override
    public Annotation resolve(Executable executable) {
        if (executable == null) {
            throw new IllegalArgumentException("executable is null");
        }

        return resolveImpl(Arrays.stream(executable.getDeclaredAnnotations()));
    }

    @Override
    public Annotation resolve(Executable executable, int paramNum) {
        if (executable == null) {
            throw new IllegalArgumentException("executable is null");
        }

//        System.out.println("pCount=" + executable.getParameterCount());
//        System.out.println(Arrays.toString(executable.getParameters()));
//        System.out.println(Arrays.toString(executable.getParameterAnnotations()));

        return resolveImpl(ReflectionAssistant.parameterAnnotations(executable, paramNum));
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
