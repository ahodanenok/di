package ahodanenok.di.interceptor;

import ahodanenok.di.DIContainer;
import ahodanenok.di.value.Value;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// todo: check if container could be injected, so there is no need to pass it as argument to lookup
public class InterceptorLookup {

    public List<Value<?>> lookup(DIContainer container, Value<?> interceptedValue, List<Value<?>> interceptors) {
        // todo: later provider for metadata resolution
        InterceptorMetadataResolution metadataResolution = container.instance(InterceptorMetadataResolution.class);

        List<Value<?>> matched = new ArrayList<>();
        Set<Annotation> bindings = metadataResolution.resolveBindings(interceptedValue.realType());
        if (!bindings.isEmpty()) {
            for (Value<?> interceptor : interceptors) {
                if (metadataResolution.resolveBindings(
                        interceptor.realType()).stream().anyMatch(bindings::contains)) {
                    matched.add(interceptor);
                }
            }
        }

        return matched;
    }
}
