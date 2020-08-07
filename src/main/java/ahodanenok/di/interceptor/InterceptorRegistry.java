package ahodanenok.di.interceptor;

import ahodanenok.di.DIContainer;
import ahodanenok.di.value.InstantiatingValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class InterceptorRegistry {

    private DIContainer container;
    private InterceptorMetadataResolution resolution;
    private LinkedHashSet<Class<?>> interceptorClasses;
    private LinkedHashSet<InstantiatingValue<?>> interceptorValues;
    private Map<Annotation, List<InstantiatingValue<?>>> interceptorsByBinding;
    private Map<Annotation, List<Method>> aroundConstructMethods;

    public InterceptorRegistry(DIContainer container, InterceptorMetadataResolution resolution) {
        this.container = container;
        this.resolution = resolution;
    }

    public void setInterceptorClasses(List<Class<?>> interceptorClasses) {
        // todo: check interceptors valid
        // todo: order of invocation for interceptors

        this.interceptorClasses = new LinkedHashSet<>(interceptorClasses);
        this.interceptorValues = new LinkedHashSet<>();
        for (Class<?> c : interceptorClasses) {
            InstantiatingValue<?> v = new InstantiatingValue<>(c);
            interceptorValues.add(v);
//
//            Set<Annotation> bindings = container.context.interceptorBindingsResolution.resolveInterceptorClass(c);
//            for (Annotation b : bindings) {
//                List<DependencyInstantiatingValue<?>> interceptors =
//                        container.interceptorsByBinding.computeIfAbsent(b, k -> new ArrayList<>());
//                interceptors.add(v);
//            }
        }
    }

    public boolean isInterceptor(Class<?> clazz) {
        return false;
    }

    /**
     * Returned interceptors are in order they should be invoked
     * @param bindings
     * @return
     */
    public List<Method> aroundConstructInterceptorMethods(Constructor<?> constructor) {
        return Collections.emptyList();
    }
}
