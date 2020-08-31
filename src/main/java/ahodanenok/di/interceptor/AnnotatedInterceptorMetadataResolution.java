package ahodanenok.di.interceptor;

import ahodanenok.di.DIContainer;
import ahodanenok.di.Later;
import ahodanenok.di.ReflectionAssistant;
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

    private DIContainer container;
    private Provider<StereotypeResolution> stereotypeResolution;

    @Inject
    public AnnotatedInterceptorMetadataResolution(DIContainer container, @Later Provider<StereotypeResolution> stereotypeResolution) {
        this.container = container;
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

    private Set<Annotation> bindings(AnnotatedElement clazz) {
        return ReflectionAssistant.annotations(clazz, ReflectionAssistant.AnnotationPresence.PRESENT)
                .filter(a -> a.annotationType().isAnnotationPresent(InterceptorBinding.class))
                .collect(Collectors.toSet());
    }
}
