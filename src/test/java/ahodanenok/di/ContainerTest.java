package ahodanenok.di;

import ahodanenok.di.value.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import javax.inject.Singleton;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
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
        InstantiatingValue<EagerSingleton> v = new InstantiatingValue<>(EagerSingleton.class);
//        v.setInitOnStartup(true);

        DIContainer.builder().addValue(v).build();
        assertTrue(EagerSingleton.init);
    }

    static class EagerNotScoped { }

    @Test
    public void testEagerInit_2() {
        InstantiatingValue<EagerNotScoped> v = new InstantiatingValue<>(EagerNotScoped.class);
//        v.setInitOnStartup(true);

        assertThrows(IllegalStateException.class, () -> DIContainer.builder().addValue(v).build());
    }

    @Eager
    @Singleton
    public static class EagerClass { }

    @Test
    public void testEagerInit_4() throws Exception {
        InstantiatingValue<EagerClass> v = new InstantiatingValue<>(EagerClass.class);
//        v.setInitOnStartup(false);

        DIContainer.builder().addValue(v).build();
//        assertFalse(v.isInitOnStartup());
    }

    @Test
    public void testEagerInit_5() throws Exception {
        InstantiatingValue<EagerClass> v = new InstantiatingValue<>(EagerClass.class);

        DIContainer.builder().addValue(v).build();
//        assertTrue(v.isInitOnStartup());
    }

    public static class EagerField {
        @Eager
        @Singleton
        static String f = "1";
    }

    @Test
    public void testEagerInit_6() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, EagerField.class.getDeclaredField("f"));
//        v.setInitOnStartup(false);

        DIContainer.builder().addValue(v).build();
//        assertFalse(v.isInitOnStartup());
    }

    @Test
    public void testEagerInit_7() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, EagerField.class.getDeclaredField("f"));

        DIContainer.builder().addValue(v).build();
//        assertTrue(v.isInitOnStartup());
    }

    public static class EagerMethod {
        @Eager
        @Singleton
        static String m() { return "test"; }
    }

    @Test
    public void testEagerInit_8() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(String.class, EagerMethod.class.getDeclaredMethod("m"));
//        v.setInitOnStartup(false);

        DIContainer.builder().addValue(v).build();
//        assertFalse(v.isInitOnStartup());
    }

    @Test
    public void testEagerInit_9() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(String.class, EagerMethod.class.getDeclaredMethod("m"));

        DIContainer.builder().addValue(v).build();
//        assertTrue(v.isInitOnStartup());
    }

    @Eager
    @Singleton
    public static class EagerProvider implements Provider<String> {
        @Override
        public String get() {
            return "default";
        }
    }

    @Test
    public void testEagerInit_10() throws Exception {
        ProviderValue<String> v = new ProviderValue<>(String.class, EagerProvider.class);
//        v.setInitOnStartup(false);

        DIContainer.builder().addValue(v).build();
//        assertFalse(v.isInitOnStartup());
    }

    @Test
    public void testDefault_1() {
        InstanceValue<Integer> v = new InstanceValue<Integer>(10);
//        v.setDefault(true);

        DIContainer container = DIContainer.builder().addValue(v).build();
        assertEquals(10, container.instance(int.class));
    }

    @Test
    public void testDefault_2() {
        InstanceValue<Integer> v1 = new InstanceValue<>(10);
//        v1.setDefault(true);

        InstanceValue<Integer> v2 = new InstanceValue<>(20);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertEquals(20, container.instance(int.class));
    }

    @Test
    public void testDefault_3() {
        InstanceValue<Integer> v1 = new InstanceValue<>(10);
//        v1.setDefault(true);

        InstanceValue<Integer> v2 = new InstanceValue<>(20);
//        v2.setDefault(true);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertThrows(IllegalStateException.class, () -> container.instance(int.class));
    }

    @Test
    public void testDefault_4() {
        InstanceValue<Integer> v1 = new InstanceValue<>(10);
//        v1.setDefault(true);

        InstanceValue<Integer> v2 = new InstanceValue<>(20);
//        v2.setDefault(true);

        InstanceValue<Integer> v3 = new InstanceValue<>(10);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).addValue(v3).build();
        assertThrows(IllegalStateException.class, () -> container.instance(int.class));
    }

    @DefaultValue
    public static class Default_1 { }

    @Test
    public void testDefault_5() {
        InstantiatingValue<Default_1> v = new InstantiatingValue<>(Default_1.class);

        DIContainer container = DIContainer.builder().addValue(v).build();
        assertNotNull(container.instance(Default_1.class));
    }

    public static class DefaultParent { }
    @DefaultValue
    public static class DefaultChildA extends DefaultParent { }
    public static class DefaultChildB extends DefaultParent { }

    @Test
    public void testDefault_6() {
        InstantiatingValue<DefaultParent> v1 = new InstantiatingValue<>(DefaultParent.class, DefaultChildA.class);
        InstantiatingValue<DefaultParent> v2 = new InstantiatingValue<>(DefaultParent.class, DefaultChildB.class);

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
        ProviderValue<String> v = new ProviderValue<>(String.class, DefaultProvider.class);

        DIContainer.builder().addValue(v).build();
//        assertTrue(v.isDefault());
    }

    @Test
    public void testDefault_8() {
        ProviderValue<String> v = new ProviderValue<>(String.class, DefaultProvider.class);
//        v.setDefault(false);

        DIContainer.builder().addValue(v).build();
//        assertFalse(v.isDefault());
    }

    public static class DefaultMethod {
        @DefaultValue
        public void m() { }
    }

    @Test
    public void testDefault_9() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(String.class, DefaultMethod.class.getDeclaredMethod("m"));

        DIContainer.builder().addValue(v).build();
//        assertTrue(v.isDefault());
    }

    @Test
    public void testDefault_10() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(String.class, DefaultMethod.class.getDeclaredMethod("m"));
//        v.setDefault(false);

        DIContainer.builder().addValue(v).build();
//        assertFalse(v.isDefault());
    }

    public static class DefaultField {
        @DefaultValue
        String f;
    }

    @Test
    public void testDefault_11() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, DefaultField.class.getDeclaredField("f"));

        DIContainer.builder().addValue(v).build();
//        assertTrue(v.isDefault());
    }

    @Test
    public void testDefault_12() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, DefaultField.class.getDeclaredField("f"));
//        v.setDefault(false);

        DIContainer.builder().addValue(v).build();
//        assertFalse(v.isDefault());
    }

    @Test
    public void testDefault_13() {
        InstantiatingValue<Default_1> v = new InstantiatingValue<>(Default_1.class);
//        v.setDefault(false);

        DIContainer.builder().addValue(v).build();
//        assertFalse(v.isDefault());
    }

    @Test
    public void testProviderByName() {
        InstanceValue<String> v1 = new InstanceValue<>("1");
//        v1.setName("v-1");

        InstanceValue<String> v2 = new InstanceValue<>("2");
//        v2.setName("v-2");

        InstanceValue<String> v3 = new InstanceValue<>("3");
//        v3.setName("v-3");

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).addValue(v3).build();

        Provider<?> v;

        v = container.provider("v-1");
        assertNotNull(v);
        assertEquals("1", v.get());

        v = container.provider("v-2");
        assertNotNull(v);
        assertEquals("2", v.get());

        v = container.provider("v-3");
        assertNotNull(v);
        assertEquals("3", v.get());

        v = container.provider("v-4");
        assertNull(v);
    }

    @Test
    public void testProviderByNameWithDefault() {
        InstanceValue<String> v1 = new InstanceValue<>("1");
//        v1.setName("v");
//        v1.setDefault(true);

        InstanceValue<String> v2 = new InstanceValue<>("2");
//        v2.setName("v");

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();

        Provider<?> v = container.provider("v");
        assertNotNull(v);
        assertEquals("2", v.get());
    }

    @Test
    public void testProviderByNameAmbiguous() {
        InstanceValue<String> v1 = new InstanceValue<>("1");
//        v1.setName("v");

        InstanceValue<String> v2 = new InstanceValue<>("2");
//        v2.setName("v");

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertThrows(RuntimeException.class, () -> container.provider("v"));
    }
}
