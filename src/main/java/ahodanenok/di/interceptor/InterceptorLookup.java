package ahodanenok.di.interceptor;

import ahodanenok.di.DIContainer;
import ahodanenok.di.value.Value;

import java.util.List;
import java.util.Set;

public interface InterceptorLookup {

    List<Value<?>> lookup(DIContainer container, Class<?> interceptedClass, Set<Value<?>> interceptors);
}
