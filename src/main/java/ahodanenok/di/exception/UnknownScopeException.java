package ahodanenok.di.exception;

import ahodanenok.di.scope.ScopeIdentifier;

public class UnknownScopeException extends DIBaseException {

    public UnknownScopeException(ScopeIdentifier scope) {
        super(String.format("Scope '%s' is not found in the container", scope));
    }
}
