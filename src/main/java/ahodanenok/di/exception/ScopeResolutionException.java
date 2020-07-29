package ahodanenok.di.exception;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ScopeResolutionException extends DependencyInjectionException {

    private String reason;

    public ScopeResolutionException(Class clazz, String reason) {
        super("Couldn't resolve scope for + " + clazz + ": " + reason);
        this.reason = reason;
    }

    public ScopeResolutionException(Method method, String reason) {
        super("Couldn't resolve scope for + " + method + ": " + reason);
        this.reason = reason;
    }

    public ScopeResolutionException(Field field, String reason) {
        super("Couldn't resolve scope for + " + field + ": " + reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
