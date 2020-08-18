package ahodanenok.di.qualifier.classes;

import ahodanenok.di.OptionalDependency;

import javax.inject.Inject;

public class ConstructorNoQualifiers {

    @Inject
    public ConstructorNoQualifiers() { }

    public ConstructorNoQualifiers(@OptionalDependency int n) { }
}
