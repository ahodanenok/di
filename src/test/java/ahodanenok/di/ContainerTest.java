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

    @Test
    public void testDefault_1() {
        DependencyInstanceValue<Integer> v = new DependencyInstanceValue<Integer>(10);
        v.setDefault(true);

        DIContainer container = DIContainer.builder().addValue(v).build();
        assertEquals(10, container.instance(int.class));
    }

    @Test
    public void testDefault_2() {
        DependencyInstanceValue<Integer> v1 = new DependencyInstanceValue<>(10);
        v1.setDefault(true);

        DependencyInstanceValue<Integer> v2 = new DependencyInstanceValue<>(20);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertEquals(20, container.instance(int.class));
    }

    @Test
    public void testDefault_3() {
        DependencyInstanceValue<Integer> v1 = new DependencyInstanceValue<>(10);
        v1.setDefault(true);

        DependencyInstanceValue<Integer> v2 = new DependencyInstanceValue<>(20);
        v2.setDefault(true);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertThrows(IllegalStateException.class, () -> container.instance(int.class));
    }

    @Test
    public void testDefault_4() {
        DependencyInstanceValue<Integer> v1 = new DependencyInstanceValue<>(10);
        v1.setDefault(true);

        DependencyInstanceValue<Integer> v2 = new DependencyInstanceValue<>(20);
        v2.setDefault(true);

        DependencyInstanceValue<Integer> v3 = new DependencyInstanceValue<>(10);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).addValue(v3).build();
        assertThrows(IllegalStateException.class, () -> container.instance(int.class));
    }
}
