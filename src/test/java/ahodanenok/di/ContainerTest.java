package ahodanenok.di;

import org.junit.jupiter.api.Test;

import javax.inject.Singleton;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    @Singleton
    static class EagerSingleton {

        static boolean init = false;

        public EagerSingleton() {
            init = true;
        }
    }

    @Test
    public void testEagerInit_1() {
        DependencyInstantiatingValue<EagerSingleton> v = new DependencyInstantiatingValue<>(EagerSingleton.class);
        v.setInitOnStartup(true);

        DIContainer.builder().addValue(v).build();
        assertTrue(EagerSingleton.init);
    }

    static class EagerNotScoped { }

    @Test
    public void testEagerInit_2() {
        DependencyInstantiatingValue<EagerNotScoped> v = new DependencyInstantiatingValue<>(EagerNotScoped.class);
        v.setInitOnStartup(true);

        assertThrows(IllegalStateException.class, () -> DIContainer.builder().addValue(v).build());
    }
}
