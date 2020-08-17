package ahodanenok.di.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Set;

public interface InjectionPoint {

    Class<?> getType();

    Set<Annotation> getQualifiers();

    Member getTarget();

    int getParameterIndex();
}
