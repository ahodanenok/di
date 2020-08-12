package ahodanenok.di.interceptor;

import ahodanenok.di.ReflectionAssistant;

import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;
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
    public boolean isInterceptor(Class<?> clazz) {
        return clazz.isAnnotationPresent(Interceptor.class);
    }

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
        if (!isInterceptor(interceptorClass)) {
            return null;
        }

        Set<Method> methods = new HashSet<>();
        for (Method m : ReflectionAssistant.methods(interceptorClass)) {
            if (m.isAnnotationPresent(AroundConstruct.class)) {
                methods.add(m);
            }
        }

        if (methods.size() > 1) {
            // todo: error, msg
            throw new IllegalStateException();
        }

        if (methods.isEmpty()) {
            return null;
        }

        Method m = methods.iterator().next();
        // aroundConstruct must have one parameter of type InvocationContext, check if there are any other variants
        if (m.getParameterCount() != 1 ||  !InvocationContext.class.isAssignableFrom(m.getParameterTypes()[0])) {
            // todo: error, msg
            throw new IllegalStateException();
        }

        return m;
    }
}
