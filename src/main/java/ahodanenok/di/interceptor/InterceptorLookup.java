package ahodanenok.di.interceptor;

import ahodanenok.di.DIContainer;
import ahodanenok.di.value.Value;

import java.util.List;

// todo: check if container could be injected, so there is no need to pass it as argument to lookup
public interface InterceptorLookup {

    List<Value<?>> lookup(DIContainer container, Value<?> interceptedValue, List<Value<?>> interceptors);
}
