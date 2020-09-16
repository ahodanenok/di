package ahodanenok.di.container.classes;

import ahodanenok.di.value.Eager;

import javax.inject.Singleton;

@Singleton
@Eager
public class EagerSingleton {

    public static boolean init = false;

    public EagerSingleton() {
        init = true;
    }
}
