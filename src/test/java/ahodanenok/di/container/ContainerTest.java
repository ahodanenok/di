package ahodanenok.di.container;

import ahodanenok.di.DIContainer;
import ahodanenok.di.DefaultValue;
import ahodanenok.di.Eager;
import ahodanenok.di.container.classes.*;
import ahodanenok.di.exception.ConfigurationException;
import ahodanenok.di.value.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import javax.inject.Singleton;

import static org.junit.jupiter.api.Assertions.*;


public class ContainerTest {

    @Test
    public void testEagerClassInitialization() {
        DIContainer.builder().addValue(new InstantiatingValue<>(EagerSingleton.class)).build();
        assertTrue(EagerSingleton.init);
    }

    @Test
    public void testErrorIfEagerIsDefinedForNonSingleton() {
        assertThrows(IllegalStateException.class, () ->
                DIContainer.builder().addValue(new InstantiatingValue<>(EagerNotScoped.class)).build());
    }

    @Test
    public void testEagerMethodProviderInitialization() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(String.class, EagerMethod.class.getDeclaredMethod("m"));
        DIContainer.builder().addValue(v).build();
        assertTrue(EagerMethod.init);
    }

    @Test
    public void  testEagerProviderInitialization() throws Exception {
        ProviderValue<String> v = new ProviderValue<>(String.class, EagerProvider.class);
        DIContainer.builder().addValue(v).build();
        assertTrue(EagerProvider.init);
    }

    @Test
    public void testEagerInitialization() throws Exception {
        DIContainer.builder()
                .addValue(new MethodProviderValue<>(String.class, EagerInitialedSequence.class.getDeclaredMethod("eagerMethod")))
                .addValue(new InstantiatingValue<>(EagerInitialedSequence.EagerClass.class))
                .build();
        assertEquals("eagerClass", EagerInitialedSequence.seq.get(0));
        assertEquals("eagerMethod", EagerInitialedSequence.seq.get(1));
    }

    @Test
    public void testOnlyDefaultResolved() {
        InstanceValue<Integer> v = new InstanceValue<>(10);
        v.metadata().setDefault(true);

        DIContainer container = DIContainer.builder().addValue(v).build();
        assertEquals(10, container.instance(int.class));
    }

    @Test
    public void testDefaultResolved() {
        InstanceValue<Integer> v1 = new InstanceValue<>(10);
        v1.metadata().setDefault(true);

        InstanceValue<Integer> v2 = new InstanceValue<>(20);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertEquals(20, container.instance(int.class));
    }

    @Test
    public void testErrorIfAllDefault() {
        InstanceValue<Integer> v1 = new InstanceValue<>(10);
        v1.metadata().setDefault(true);

        InstanceValue<Integer> v2 = new InstanceValue<>(20);
        v2.metadata().setDefault(true);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertThrows(ConfigurationException.class, () -> container.instance(int.class));
    }

    @Test
    public void testErrorIfMultipleDefault() {
        InstanceValue<Integer> v1 = new InstanceValue<>(10);
        v1.metadata().setDefault(true);

        InstanceValue<Integer> v2 = new InstanceValue<>(20);
        v2.metadata().setDefault(true);

        InstanceValue<Integer> v3 = new InstanceValue<>(10);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).addValue(v3).build();
        assertThrows(ConfigurationException.class, () -> container.instance(int.class));
    }

    @Test
    public void testDefault_6() {
        InstantiatingValue<DefaultParent> v1 = new InstantiatingValue<>(DefaultParent.class, DefaultChildA.class);
        InstantiatingValue<DefaultParent> v2 = new InstantiatingValue<>(DefaultParent.class, DefaultChildB.class);

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertTrue(container.instance(DefaultParent.class) instanceof DefaultChildB);
    }

    @Test
    public void testProviderByName() {
        InstanceValue<String> v1 = new InstanceValue<>("1");
        v1.metadata().setName("v-1");

        InstanceValue<String> v2 = new InstanceValue<>("2");
        v2.metadata().setName("v-2");

        InstanceValue<String> v3 = new InstanceValue<>("3");
        v3.metadata().setName("v-3");

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
        v1.metadata().setName("v");
        v1.metadata().setDefault(true);

        InstanceValue<String> v2 = new InstanceValue<>("2");
        v2.metadata().setName("v");

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();

        Provider<?> v = container.provider("v");
        assertNotNull(v);
        assertEquals("2", v.get());
    }

    @Test
    public void testProviderByNameAmbiguous() {
        InstanceValue<String> v1 = new InstanceValue<>("1");
        v1.metadata().setName("v");

        InstanceValue<String> v2 = new InstanceValue<>("2");
        v2.metadata().setName("v");

        DIContainer container = DIContainer.builder().addValue(v1).addValue(v2).build();
        assertThrows(RuntimeException.class, () -> container.provider("v"));
    }

    @Test
    public void testPostConstructInvokedAfterDependenciesInjected() {
        DIContainer container = DIContainer.builder()
                .addValue(new InstanceValue<>("1"))
                .addValue(new InstanceValue<>(3.14f))
                .addValue(new InstanceValue<>(10))
                .addValue(new InstantiatingValue<>(ClassWithPostConstruct.class))
                .build();

        ClassWithPostConstruct obj = container.instance(ClassWithPostConstruct.class);
        assertNotNull(obj.objects);
        assertEquals(10, obj.objects[0]);
        assertEquals("1", obj.objects[1]);
        assertEquals(3.14f, obj.objects[2]);
    }

    @Test
    public void testPreDestroyInvoked() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithPreDestroy.class))
                .build();

        ClassWithPreDestroy obj = container.instance(ClassWithPreDestroy.class);
        container.close();
        assertTrue(obj.destroyed);
    }
}
