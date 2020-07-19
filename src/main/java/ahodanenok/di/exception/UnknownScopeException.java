package ahodanenok.di.exception;

import ahodanenok.di.scope.ScopeIdentifier;

public class UnknownScopeException extends DependencyInjectionException {

    public UnknownScopeException(ScopeIdentifier scope) {
        super("Unknown scope " + scope);
    }
}
