package ahodanenok.di.container.classes;

import ahodanenok.di.value.Eager;

import javax.inject.Singleton;

public class EagerMethod {

    public static boolean init;

    @Eager
    @Singleton
    static String m() {
        init = true;
        return "test";
    }
}
