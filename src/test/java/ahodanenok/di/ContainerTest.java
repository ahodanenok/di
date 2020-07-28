package ahodanenok.di;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

public class ContainerTest {

    class A {
        public final int f = 1;
    }

    class B extends A {
        public int f;
    }

    @Test
    public void a() {
        System.out.println(ReflectionAssistant.fields(B.class).collect(Collectors.toList()));
    }
}
