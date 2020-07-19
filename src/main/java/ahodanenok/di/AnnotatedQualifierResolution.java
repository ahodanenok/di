package ahodanenok.di;

import ahodanenok.di.exception.QualifierResolutionException;

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

        Set<Annotation> q = qualifiers(Arrays.stream(clazz.getDeclaredAnnotations()));
        if (q.size() > 1) {
            throw new QualifierResolutionException(clazz, "multiple qualifiers");
        }

        if (q.size() == 1) {
            return q.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public Annotation resolve(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("field is null");
        }

        Set<Annotation> q = qualifiers(Arrays.stream(field.getDeclaredAnnotations()));
        if (q.size() > 1) {
            throw new QualifierResolutionException(field, "multiple qualifiers");
        }

        if (q.size() == 1) {
            return q.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public Annotation resolve(Executable executable) {
        if (executable == null) {
            throw new IllegalArgumentException("executable is null");
        }

        Set<Annotation> q = qualifiers(Arrays.stream(executable.getDeclaredAnnotations()));
        if (q.size() > 1) {
            throw new QualifierResolutionException(executable, "multiple qualifiers");
        }

        if (q.size() == 1) {
            return q.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public Annotation resolve(Executable executable, int paramNum) {
        if (executable == null) {
            throw new IllegalArgumentException("executable is null");
        }

        Set<Annotation> q = qualifiers(ReflectionAssistant.parameterAnnotations(executable, paramNum));
        if (q.size() > 1) {
            throw new QualifierResolutionException(executable, paramNum, "multiple qualifiers");
        }

        if (q.size() == 1) {
            return q.iterator().next();
        } else {
            return null;
        }
    }

    private Set<Annotation> qualifiers(Stream<Annotation> annotations) {
        return annotations
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toSet());
    }
}
