package ahodanenok.di.exception;

import ahodanenok.di.DependencyIdentifier;
import ahodanenok.di.Injectable;

public class UnsatisfiedDependencyException extends DependencyInjectionException{

    private Injectable injectable;
    private DependencyIdentifier<?> id;
    private String reason;

    public UnsatisfiedDependencyException(DependencyIdentifier<?> id, String reason) {
        super("Unsatisfied dependency " + id + ": " + reason);
        this.id = id;
        this.reason = reason;
    }

    public UnsatisfiedDependencyException(Injectable injectable, DependencyIdentifier<?> id, String reason) {
        super("Unsatisfied dependency " + id + " for " + injectable + ": " + reason);
        this.injectable = injectable;
        this.id = id;
        this.reason = reason;
    }

    public Injectable getInjectable() {
        return injectable;
    }

    public DependencyIdentifier<?> getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }
}
