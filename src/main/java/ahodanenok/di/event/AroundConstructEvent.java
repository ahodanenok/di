package ahodanenok.di.event;

import ahodanenok.di.interceptor.AroundConstruct;
import ahodanenok.di.value.Value;

public final class AroundConstructEvent<T> implements Event {

    private final Value<?> ownerValue;
    private final AroundConstruct<T> aroundConstruct;

    public AroundConstructEvent(Value<?> ownerValue, AroundConstruct<T> aroundConstruct) {
        this.ownerValue = ownerValue;
        this.aroundConstruct = aroundConstruct;
    }

    public Value<?> getOwnerValue() {
        return ownerValue;
    }

    public AroundConstruct<T> getAroundConstruct() {
        return aroundConstruct;
    }
}
