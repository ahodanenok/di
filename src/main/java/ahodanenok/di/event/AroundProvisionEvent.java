package ahodanenok.di.event;

import ahodanenok.di.interceptor.AroundProvision;
import ahodanenok.di.value.Value;

public final class AroundProvisionEvent implements Event {

    private final AroundProvision aroundProvision;
    private final Value<?> applicant;

    public AroundProvisionEvent(Value<?> applicant, AroundProvision aroundProvision) {
        this.applicant = applicant;
        this.aroundProvision = aroundProvision;
    }

    public AroundProvision getAroundProvision() {
        return aroundProvision;
    }

    public Value<?> getApplicant() {
        return applicant;
    }
}
