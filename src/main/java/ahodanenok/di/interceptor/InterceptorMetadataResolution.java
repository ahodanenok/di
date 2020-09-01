package ahodanenok.di.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

// todo: think about collapsing resolveBindings for member elements to resolveBindings(Member)
public interface InterceptorMetadataResolution {

    boolean isInterceptor(Class<?> clazz);

    Set<Annotation> resolveBindings(Class<?> clazz);

    Set<Annotation> resolveBindings(Constructor<?> constructor);

    Set<Annotation> resolveBindings(Method constructor);

    Set<Annotation> resolveBindings(Field field);

    Method resolveAroundConstruct(Class<?> interceptorClass);
}
