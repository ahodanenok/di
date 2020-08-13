package ahodanenok.di.scope.classes;

import ahodanenok.di.scope.NotScoped;

import javax.inject.Singleton;

@Singleton
public class NotScopedMethod {

    @NotScoped
    void method() { }
}
