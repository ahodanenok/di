package ahodanenok.di.event;

import ahodanenok.di.interceptor.AroundInject;
import ahodanenok.di.value.Value;

public class AroundInjectEvent implements Event {

    private final AroundInject aroundInject;
    private final Value<?> applicant;

    public AroundInjectEvent(Value<?> applicant, AroundInject aroundInject) {
        this.applicant = applicant;
        this.aroundInject = aroundInject;
    }

    public AroundInject getAroundInject() {
        return aroundInject;
    }

    public Value<?> getApplicant() {
        return applicant;
    }
}
