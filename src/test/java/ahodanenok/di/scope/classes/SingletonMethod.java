package ahodanenok.di.scope.classes;

import ahodanenok.di.scope.NotScoped;

import javax.inject.Singleton;

@NotScoped
public class SingletonMethod {

    @Singleton
    void method() { }
}
