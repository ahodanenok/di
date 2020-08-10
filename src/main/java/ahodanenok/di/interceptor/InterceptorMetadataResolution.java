package ahodanenok.di.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

public interface InterceptorMetadataResolution {

    Set<Annotation> resolveBindings(Class<?> clazz, Supplier<Set<Annotation>> stereotypes);

    Set<Annotation> resolveBindings(Constructor<?> constructor, Supplier<Set<Annotation>> stereotypes);

    Method resolveAroundConstruct(Class<?> interceptorClass);
}
