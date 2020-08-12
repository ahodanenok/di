package ahodanenok.di.interceptor;

import ahodanenok.di.ReflectionAssistant;

import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AnnotatedInterceptorMetadataResolution implements InterceptorMetadataResolution {

    @Override
    public boolean isInterceptor(Class<?> clazz) {
        return clazz.isAnnotationPresent(Interceptor.class);
    }

    @Override
    public Set<Annotation> resolveBindings(Constructor<?> constructor, Supplier<Set<Annotation>> stereotypes) {
        return resolveExecutableBindings(constructor, stereotypes);
    }

    @Override
    public Set<Annotation> resolveBindings(Method method, Supplier<Set<Annotation>> stereotypes) {
        return resolveExecutableBindings(method, stereotypes);
    }

    @Override
    public Set<Annotation> resolveBindings(Class<?> clazz, Supplier<Set<Annotation>> stereotypes) {
        return resolveBindingsFromQueue(new LinkedList<>(bindings(clazz)), stereotypes);
    }

    private Set<Annotation> resolveExecutableBindings(Executable executable, Supplier<Set<Annotation>> stereotypes) {
        LinkedList<Annotation> queue = new LinkedList<>();
        queue.addAll(Arrays.stream(executable.getDeclaredAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(InterceptorBinding.class))
                .collect(Collectors.toSet()));
        queue.addAll(bindings(executable.getDeclaringClass()));

        return resolveBindingsFromQueue(queue, stereotypes);
    }

    private Set<Annotation> resolveBindingsFromQueue(LinkedList<Annotation> queue, Supplier<Set<Annotation>> stereotypes) {
        Set<Annotation> bindings = new HashSet<>();

        for (Annotation s : stereotypes.get()) {
            queue.addAll(bindings(s.annotationType()));
        }

        while (!queue.isEmpty()) {
            Annotation b = queue.removeFirst();
            if (!bindings.add(b)) {
                continue;
            }

            queue.addAll(bindings(b.annotationType()));
        }

        return bindings;
    }

    @Override
    public Method resolveAroundConstruct(Class<?> interceptorClass) {
        Set<Method> methods = new HashSet<>();
        for (Method m : ReflectionAssistant.methods(interceptorClass)) {
            if (m.isAnnotationPresent(AroundConstruct.class)) {
                methods.add(m);
            }
        }

        if (!methods.isEmpty() && !isInterceptor(interceptorClass)) {
            // todo: error, msg
            throw new IllegalStateException();
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

        int modifiers = m.getModifiers();
        if (Modifier.isStatic(modifiers) || Modifier.isAbstract(modifiers) || Modifier.isFinal(modifiers)) {
            // todo: error
            throw new IllegalStateException("Around construct method must not be declared as abstract, final, or static.");
        }

        return m;
    }

    private Set<Annotation> bindings(Class<?> clazz) {
        return ReflectionAssistant.annotations(clazz, ReflectionAssistant.AnnotationPresence.PRESENT)
                .filter(a -> a.annotationType().isAnnotationPresent(InterceptorBinding.class))
                .collect(Collectors.toSet());
    }
}
