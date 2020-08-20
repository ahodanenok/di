package ahodanenok.di.exception;

import ahodanenok.di.ValueSpecifier;
import ahodanenok.di.Injectable;

public class UnsatisfiedDependencyException extends DependencyInjectionException{

    private Injectable injectable;
    private ValueSpecifier<?> id;
    private String reason;

    public UnsatisfiedDependencyException(ValueSpecifier<?> id, String reason) {
        super("Unsatisfied dependency " + id + ": " + reason);
        this.id = id;
        this.reason = reason;
    }

    public UnsatisfiedDependencyException(Injectable injectable, ValueSpecifier<?> id, String reason) {
        super("Unsatisfied dependency " + id + " for " + injectable + ": " + reason);
        this.injectable = injectable;
        this.id = id;
        this.reason = reason;
    }

    public Injectable getInjectable() {
        return injectable;
    }

    public ValueSpecifier<?> getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }
}
