package ahodanenok.di.interceptor;

import java.util.function.Consumer;

public class AroundInject {

    private final InjectionPoint injectionPoint;
    private final Consumer<Object> dependencyConsumer;
    private Object resolvedDependency;

    public AroundInject(InjectionPoint injectionPoint, Consumer<Object> dependencyConsumer) {
        this.injectionPoint = injectionPoint;
        this.dependencyConsumer = dependencyConsumer;
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public void setResolvedDependency(Object resolvedDependency) {
        this.resolvedDependency = resolvedDependency;
    }

    public Object getResolvedDependency() {
        return resolvedDependency;
    }

    public void proceed() {
        dependencyConsumer.accept(resolvedDependency);
    }
}
