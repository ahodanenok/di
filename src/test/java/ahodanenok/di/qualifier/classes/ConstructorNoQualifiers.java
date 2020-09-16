package ahodanenok.di.qualifier.classes;

import ahodanenok.di.Optional;

import javax.inject.Inject;

public class ConstructorNoQualifiers {

    @Inject
    public ConstructorNoQualifiers() { }

    public ConstructorNoQualifiers(@Optional int n) { }
}
