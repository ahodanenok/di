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
    public Set<Annotation> resolve(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        return qualifiers(ReflectionAssistant.annotations(clazz, ReflectionAssistant.AnnotationPresence.ASSOCIATED));
    }

    @Override
    public Set<Annotation> resolve(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("field is null");
        }

        return qualifiers(ReflectionAssistant.annotations(field, ReflectionAssistant.AnnotationPresence.ASSOCIATED));
    }

    @Override
    public Set<Annotation> resolve(Executable executable) {
        if (executable == null) {
            throw new IllegalArgumentException("executable is null");
        }

        return qualifiers(ReflectionAssistant.annotations(executable, ReflectionAssistant.AnnotationPresence.ASSOCIATED));
    }

    @Override
    public Set<Annotation> resolve(Executable executable, int paramNum) {
        if (executable == null) {
            throw new IllegalArgumentException("executable is null");
        }

        return qualifiers(ReflectionAssistant.parameterAnnotations(executable, paramNum, ReflectionAssistant.AnnotationPresence.INDIRECTLY));
    }

    private Set<Annotation> qualifiers(Stream<? extends Annotation> annotations) {
        return annotations
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toSet());
    }
}
