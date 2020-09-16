package ahodanenok.di.container.classes;

import ahodanenok.di.value.Eager;

import javax.inject.Singleton;

public class EagerField {

    @Eager
    @Singleton
    static String f = "1";
}

