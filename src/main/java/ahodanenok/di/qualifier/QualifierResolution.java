package ahodanenok.di.qualifier;

import ahodanenok.di.ReflectionAssistant;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Set;
import java.util.stream.Collectors;

public final class QualifierResolution {

    public Set<Annotation> resolve(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            throw new IllegalArgumentException("element is null");
        }

        ReflectionAssistant.AnnotationPresence presence;
        if (annotatedElement instanceof Parameter) {
            presence = ReflectionAssistant.AnnotationPresence.INDIRECTLY;
        } else {
            presence = ReflectionAssistant.AnnotationPresence.ASSOCIATED;
        }

        return ReflectionAssistant.annotations(annotatedElement, presence)
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toSet());
    }
}
