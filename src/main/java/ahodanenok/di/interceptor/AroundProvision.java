package ahodanenok.di.interceptor;

import java.util.function.Consumer;

public class AroundProvision {

    private final InjectionPoint injectionPoint;
    private final Consumer<Object> dependencyConsumer;
    private Object resolvedDependency;

    public AroundProvision(InjectionPoint injectionPoint, Consumer<Object> dependencyConsumer) {
        this.injectionPoint = injectionPoint;
        this.dependencyConsumer = dependencyConsumer;
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    // todo: is this property needed?
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
