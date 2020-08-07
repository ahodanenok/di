package ahodanenok.di.interceptor;

import javax.interceptor.AroundConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public class AnnotatedInterceptorMetadataResolution implements InterceptorMetadataResolution {

    @Override
    public Set<Annotation> resolveBindings(Constructor<?> constructor) {
        return Collections.emptySet();
    }

    @Override
    public Set<Annotation> resolveBindings(Class<?> clazz) {
        return Collections.emptySet();
    }

    @Override
    public Method resolveAroundConstruct(Class<?> interceptorClass) {
        for (Method m : interceptorClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(AroundConstruct.class)) {
                return m;
            }
        }

        return null;
    }
}
