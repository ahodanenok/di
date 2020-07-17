package ahodanenok.di.exception;

import ahodanenok.di.scope.ScopeIdentifier;

import java.lang.reflect.Method;

public class ScopeResolveException extends DependencyInjectionException {

    private ScopeIdentifier scope;
    private String reason;

    public ScopeResolveException(Class clazz, String reason) {
        super("Couldn't resolve scope for + " + clazz + ": " + reason);
        this.reason = reason;
    }

    public ScopeResolveException(Method method, String reason) {
        super("Couldn't resolve scope for + " + method + ": " + reason);
        this.reason = reason;
    }

    public ScopeResolveException(ScopeIdentifier scope, String reason) {
        super("Couldn't resolve scope " + scope + ": " + reason);
        this.scope = scope;
        this.reason = reason;
    }

    public ScopeIdentifier getScope() {
        return scope;
    }

    public String getReason() {
        return reason;
    }
}
