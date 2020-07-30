package ahodanenok.di;

import org.junit.jupiter.api.Test;

import javax.inject.Provider;
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

    @Eager
    @Singleton
    public static class EagerClass { }

    @Test
    public void testEagerInit_4() throws Exception {
        DependencyInstantiatingValue<EagerClass> v = new DependencyInstantiatingValue<>(EagerClass.class);
        v.setInitOnStartup(false);

        DIContainer.builder().addValue(v).build();
        assertFalse(v.isInitOnStartup());
    }

    @Test
    public void testEagerInit_5() throws Exception {
        DependencyInstantiatingValue<EagerClass> v = new DependencyInstantiatingValue<>(EagerClass.class);

        DIContainer.builder().addValue(v).build();
        assertTrue(v.isInitOnStartup());
    }

    public static class EagerField {
        @Eager
        @Singleton
        static String f = "1";
    }

    @Test
    public void testEagerInit_6() throws Exception {
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(String.class, EagerField.class.getDeclaredField("f"));
        v.setInitOnStartup(false);

        DIContainer.builder().addValue(v).build();
        assertFalse(v.isInitOnStartup());
    }

    @Test
    public void testEagerInit_7() throws Exception {
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(String.class, EagerField.class.getDeclaredField("f"));

        DIContainer.builder().addValue(v).build();
        assertTrue(v.isInitOnStartup());
    }

    public static class EagerMethod {
        @Eager
        @Singleton
        static String m() { return "test"; }
    }

    @Test
    public void testEagerInit_8() throws Exception {
        DependencyMethodProviderValue<String> v = new DependencyMethodProviderValue<>(String.class, EagerMethod.class.getDeclaredMethod("m"));
        v.setInitOnStartup(false);

        DIContainer.builder().addValue(v).build();
        assertFalse(v.isInitOnStartup());
    }

    @Test
    public void testEagerInit_9() throws Exception {
        DependencyMethodProviderValue<String> v = new DependencyMethodProviderValue<>(String.class, EagerMethod.class.getDeclaredMethod("m"));

        DIContainer.builder().addValue(v).build();
        assertTrue(v.isInitOnStartup());
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

    @DefaultValue
    public static class Default_1 { }

    @Test
    public void testDefault_5() {
        DependencyInstantiatingValue<Default_1> v = new DependencyInstantiatingValue<>(Default_1.class);

        DIContainer container = DIContainer.builder().addValue(v).build();
        assertNotNull(container.instance(Default_1.class));
    }

    public static class DefaultParent { }
    @DefaultValue
    public static class DefaultChildA extends DefaultParent { }
    public static class DefaultChildB extends DefaultParent { }

    @Test
    public void testDefault_6() {
        DependencyInstantiatingValue<DefaultParent> v1 = new DependencyInstantiatingValue<>(DefaultParent.class, DefaultChildA.class);
        DependencyInstantiatingValue<DefaultParent> v2 = new DependencyInstantiatingValue<>(DefaultParent.class, DefaultChildB.class);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertTrue(container.instance(DefaultParent.class) instanceof DefaultChildB);
    }

    @DefaultValue
    public static class DefaultProvider implements Provider<String> {
        @Override
        public String get() {
            return "default";
        }
    }

    @Test
    public void testDefault_7() {
        DependencyProviderValue<String> v = new DependencyProviderValue<>(String.class, DefaultProvider.class);

        DIContainer.builder().addValue(v).build();
        assertTrue(v.isDefault());
    }

    public static class DefaultMethod {
        @DefaultValue
        public void m() { }
    }

    @Test
    public void testDefault_8() throws Exception {
        DependencyMethodProviderValue<String> v = new DependencyMethodProviderValue<>(String.class, DefaultMethod.class.getDeclaredMethod("m"));

        DIContainer.builder().addValue(v).build();
        assertTrue(v.isDefault());
    }

    public static class DefaultField {
        @DefaultValue
        String f;
    }

    @Test
    public void testDefault_9() throws Exception {
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(String.class, DefaultField.class.getDeclaredField("f"));

        DIContainer.builder().addValue(v).build();
        assertTrue(v.isDefault());
    }
}
