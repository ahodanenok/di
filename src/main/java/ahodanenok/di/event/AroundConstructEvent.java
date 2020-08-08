package ahodanenok.di.event;

import ahodanenok.di.interceptor.AroundConstruct;

public class AroundConstructEvent<T> implements Event {

    private AroundConstruct<T> aroundConstruct;

    public AroundConstructEvent(AroundConstruct<T> aroundConstruct) {
        this.aroundConstruct = aroundConstruct;
    }

    public AroundConstruct<T> getAroundConstruct() {
        return aroundConstruct;
    }
}
