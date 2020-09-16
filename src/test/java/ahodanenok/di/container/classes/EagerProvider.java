package ahodanenok.di.container.classes;

import ahodanenok.di.value.Eager;

import javax.inject.Provider;
import javax.inject.Singleton;

@Eager
@Singleton
public class EagerProvider implements Provider<String> {

    public static boolean init;

    @Override
    public String get() {
        init = true;
        return "default";
    }
}
