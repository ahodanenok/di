package ahodanenok.di.interceptor;

import ahodanenok.di.Later;
import ahodanenok.di.ReflectionAssistant;
import ahodanenok.di.exception.ConfigurationException;
import ahodanenok.di.stereotype.StereotypeResolution;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AnnotatedInterceptorMetadataResolution implements InterceptorMetadataResolution {

    private final Provider<StereotypeResolution> stereotypeResolution;

    @Inject
    public AnnotatedInterceptorMetadataResolution(@Later Provider<StereotypeResolution> stereotypeResolution) {
        this.stereotypeResolution = stereotypeResolution;
    }

    @Override
    public boolean isInterceptor(Class<?> clazz) {
        return clazz.isAnnotationPresent(Interceptor.class);
    }

    @Override
    public Set<Annotation> resolveBindings(Constructor<?> constructor) {
        Set<Annotation> bindings = resolveBindingsFromQueue(new LinkedList<>(bindings(constructor)), () -> stereotypeResolution.get().resolve(constructor));
        bindings.addAll(resolveBindings(constructor.getDeclaringClass()));
        return bindings;
    }

    @Override
    public Set<Annotation> resolveBindings(Method method) {
        Set<Annotation> bindings = resolveBindingsFromQueue(new LinkedList<>(bindings(method)), () -> stereotypeResolution.get().resolve(method));
        bindings.addAll(resolveBindings(method.getDeclaringClass()));
        return bindings;
    }

    @Override
    public Set<Annotation> resolveBindings(Field field) {
        return resolveBindingsFromQueue(new LinkedList<>(bindings(field)), () -> stereotypeResolution.get().resolve(field));
    }

    @Override
    public Set<Annotation> resolveBindings(Class<?> clazz) {
        return resolveBindingsFromQueue(new LinkedList<>(bindings(clazz)), () -> stereotypeResolution.get().resolve(clazz));
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

        if (methods.size() > 1) {
            throw new ConfigurationException("Multiple @AroundConstruct methods are declared in the class "
                    + interceptorClass + ": " + methods);
        }

        if (!methods.isEmpty() && !isInterceptor(interceptorClass)) {
            throw new ConfigurationException("@AroundConstruct method " + methods.iterator().next()
                    + " is declared on a non-interceptor class " + interceptorClass);
        }

        if (methods.isEmpty()) {
            return null;
        }

        Method m = methods.iterator().next();
        // aroundConstruct must have one parameter of type InvocationContext, todo: check if there are any other variants
        if (m.getParameterCount() != 1 ||  !InvocationContext.class.isAssignableFrom(m.getParameterTypes()[0])) {
            throw new ConfigurationException("@AroundConstruct method must accept one parameter "
                    + "of type javax.interceptor.InvocationContext: " + m + " in the class " + interceptorClass);
        }

        int modifiers = m.getModifiers();
        if (Modifier.isStatic(modifiers) || Modifier.isAbstract(modifiers) || Modifier.isFinal(modifiers)) {
            throw new ConfigurationException(
                    "@AroundConstruct method must not be declared as abstract, final, or static: "
                            + m + " in the class " + interceptorClass);
        }

        return m;
    }

    private Set<Annotation> bindings(AnnotatedElement clazz) {
        return ReflectionAssistant.annotations(clazz, ReflectionAssistant.AnnotationPresence.PRESENT)
                .filter(a -> a.annotationType().isAnnotationPresent(InterceptorBinding.class))
                .collect(Collectors.toSet());
    }
}
