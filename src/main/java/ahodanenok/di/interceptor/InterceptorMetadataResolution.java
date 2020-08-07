package ahodanenok.di.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

public interface InterceptorMetadataResolution {

    Set<Annotation> resolveBindings(Class<?> clazz);

    Set<Annotation> resolveBindings(Constructor<?> constructor);

    Method resolveAroundConstruct(Class<?> interceptorClass);
}
