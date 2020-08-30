package ahodanenok.di.container.classes;

import ahodanenok.di.Eager;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

public class EagerInitialedSequence {

    public final static List<String> seq = new ArrayList<>();

    @Eager(phase = 1)
    @Singleton
    public static class EagerClass {
        public EagerClass() {
            seq.add("eagerClass");
        }
    }

    @Eager(phase = 2)
    @Singleton
    static String eagerMethod() {
        seq.add("eagerMethod");
        return "test";
    }
}
