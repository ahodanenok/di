package ahodanenok.di.qualifier;

import ahodanenok.di.ReflectionAssistant;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class QualifierResolution {

    public Set<Annotation> resolve(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        return qualifiers(ReflectionAssistant.annotations(clazz, ReflectionAssistant.AnnotationPresence.ASSOCIATED));
    }

    public Set<Annotation> resolve(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("field is null");
        }

        return qualifiers(ReflectionAssistant.annotations(field, ReflectionAssistant.AnnotationPresence.ASSOCIATED));
    }

    public Set<Annotation> resolve(Executable executable) {
        if (executable == null) {
            throw new IllegalArgumentException("executable is null");
        }

        return qualifiers(ReflectionAssistant.annotations(executable, ReflectionAssistant.AnnotationPresence.ASSOCIATED));
    }

    public Set<Annotation> resolve(Executable executable, int paramNum) {
        if (executable == null) {
            throw new IllegalArgumentException("executable is null");
        }

        if (paramNum < 0 || paramNum >= executable.getParameterCount()) {
            throw new IllegalArgumentException(
                    String.format("executable has %d parameter(s), given parameter index: %d", executable.getParameterCount(), paramNum));
        }

//        return qualifiers(ReflectionAssistant.parameterAnnotations(executable, paramNum, ReflectionAssistant.AnnotationPresence.INDIRECTLY));
        return qualifiers(ReflectionAssistant.annotations(executable.getParameters()[paramNum], ReflectionAssistant.AnnotationPresence.INDIRECTLY));
    }

    private Set<Annotation> qualifiers(Stream<? extends Annotation> annotations) {
        return annotations
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toSet());
    }
}
