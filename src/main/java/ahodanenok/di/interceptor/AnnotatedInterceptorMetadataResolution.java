package ahodanenok.di.interceptor;

import ahodanenok.di.ReflectionAssistant;

import javax.interceptor.AroundConstruct;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotatedInterceptorMetadataResolution implements InterceptorMetadataResolution {

    @Override
    public Set<Annotation> resolveBindings(Constructor<?> constructor, Supplier<Set<Annotation>> stereotypes) {
        return resolveBindings(constructor.getDeclaringClass(), stereotypes);
    }

    @Override
    public Set<Annotation> resolveBindings(Class<?> clazz, Supplier<Set<Annotation>> stereotypes) {
        Stream<? extends Annotation> bindings = ReflectionAssistant.annotations(
                    clazz, ReflectionAssistant.AnnotationPresence.PRESENT, InterceptorBinding.class);

        for (Annotation s : stereotypes.get()) {
            bindings = Stream.concat(
                    bindings,
                    ReflectionAssistant.annotations(
                            s.annotationType(),
                            ReflectionAssistant.AnnotationPresence.DIRECTLY,
                            InterceptorBinding.class));
        }

        return bindings.collect(Collectors.toSet());
    }

    @Override
    public Method resolveAroundConstruct(Class<?> interceptorClass) {
        Set<Method> methods = new HashSet<>();
        for (Method m : interceptorClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(AroundConstruct.class)) {
                methods.add(m);
            }
        }

        if (methods.size() > 1) {
            // todo: error, msg
            throw new IllegalStateException();
        }

        if (methods.size() == 1) {
            return methods.iterator().next();
        } else {
            return null;
        }
    }
}