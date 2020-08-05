package ahodanenok.di;

import ahodanenok.di.exception.DependencyInjectionException;
import ahodanenok.di.exception.DependencyInstantiatingException;
import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.*;

public class InstantiatingValueTest {

    private static DIContainer container;

    @BeforeAll
    public static void createContainer() {
        container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(10))
                .build();
    }

    @Test
    public void testInstanceClass_1() {
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(TestInterface.class));
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(TestInterface.class, TestInterface.class));
    }

    @Test
    public void testInstanceClass_2() {
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(int.class));
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(int.class, int.class));
    }

    @Test
    public void testInstanceClass_3() {
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(byte[].class));
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(byte[].class, byte[].class));
    }

    @Test
    public void testInstanceClass_4() {
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(Qualifier.class));
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(Qualifier.class, Qualifier.class));
    }

    enum TestEnum { }
    @Test
    public void testInstanceClass_5() {
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(TestEnum.class));
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstantiatingValue<>(TestEnum.class, TestEnum.class));
    }

    @Test
    public void testQualifiers_1() {
        DependencyInstantiatingValue<TestInstance> v =
                new DependencyInstantiatingValue<>(TestInstance.class);
        v.bind(container.getContext());
        assertEquals(1, v.id().qualifiers().size());
        assertEquals(TestQualifier_Class.class, v.id().qualifiers().iterator().next().annotationType());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
    }

    @Test
    public void testQualifiers_2() {
        DependencyInstantiatingValue<TestInterface> v =
                new DependencyInstantiatingValue<>(TestInterface.class, TestInstance.class);
        v.bind(container.getContext());
        assertEquals(1, v.id().qualifiers().size());
        assertEquals(TestQualifier_Class.class, v.id().qualifiers().iterator().next().annotationType());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
    }

    @Test
    public void testQualifiers_3() {
        DependencyInstantiatingValue<TestInstance> v =
                new DependencyInstantiatingValue<>(DependencyIdentifier.of(TestInstance.class), TestInstance.class);
        v.bind(container.getContext());
        assertTrue(v.id().qualifiers().isEmpty());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
    }

    @Test
    public void testScope_1() {
        DependencyInstantiatingValue<TestInstanceScope> v =
                new DependencyInstantiatingValue<>(TestInstanceScope.class);
        v.bind(container.getContext());
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
    }

    @Test
    public void testScope_2() {
        DependencyInstantiatingValue<TestInterface> v =
                new DependencyInstantiatingValue<>(TestInterface.class, TestInstanceScope.class);
        v.bind(container.getContext());
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
    }

    @Test
    public void testScope_3() {
        DependencyInstantiatingValue<TestInstanceScope> v =
                new DependencyInstantiatingValue<>(DependencyIdentifier.of(TestInstanceScope.class), TestInstanceScope.class);
        v.bind(container.getContext());
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
    }

    @Test
    public void testDefaultConstructor_1() {
        DependencyInstantiatingValue<TestClass_Default> v =
                new DependencyInstantiatingValue<>(TestClass_Default.class);
        v.bind(container.getContext());

        assertNotNull(v.provider().get());
    }

    @Test
    public void testDefaultConstructor_2() {
        DependencyInstantiatingValue<TestClass_Default_NotPublic> v =
                new DependencyInstantiatingValue<>(TestClass_Default_NotPublic.class);
        v.bind(container.getContext());

        assertThrows(DependencyInjectionException.class, () -> v.provider().get());
    }

    @Test
    public void testDefaultConstructor_3() {
        DependencyInstantiatingValue<TestClass_Default_NotSingle> v =
                new DependencyInstantiatingValue<>(TestClass_Default_NotSingle.class);
        v.bind(container.getContext());

        assertThrows(DependencyInjectionException.class, () -> v.provider().get());
    }

    @Test
    public void testMultipleConstructors_1() {
        DependencyInstantiatingValue<TestClass_Multiple> v =
                new DependencyInstantiatingValue<>(TestClass_Multiple.class);
        v.bind(container.getContext());

        assertThrows(DependencyInstantiatingException.class, () -> v.provider().get());
    }


    @Test
    public void testMultipleConstructors_2() {
        DependencyInstantiatingValue<TestInject_MultipleInject> v =
                new DependencyInstantiatingValue<>(TestInject_MultipleInject.class);
        v.bind(container.getContext());

       assertEquals(10, v.provider().get().n);
    }

    public static class NestedClass { }

    @Test
    public void testNestedClass_1() {
        DependencyInstantiatingValue<NestedClass> v =
                new DependencyInstantiatingValue<>(NestedClass.class);
        v.bind(container.getContext());

        assertNotNull(v.provider().get());
    }
}

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface TestQualifier_Interface { }

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface TestQualifier_Class { }

@TestQualifier_Interface
interface TestInterface { }

@Singleton
class TestInstanceScope implements TestInterface { }

@TestQualifier_Class
class TestInstance implements TestInterface { }

class TestClass_Default {
    public TestClass_Default() { }
}

class TestClass_Default_NotPublic {
    TestClass_Default_NotPublic() { }
}
class TestClass_Default_NotSingle{
    public TestClass_Default_NotSingle() { }
    public TestClass_Default_NotSingle(int a) { }
}


class TestClass_Multiple {
    public TestClass_Multiple(String n) { }
    public TestClass_Multiple(int a) { }
}
class TestInject_MultipleInject {
    int n;

    TestInject_MultipleInject(String n) { }
    @Inject TestInject_MultipleInject(int a) {
        this.n = a;
    }
}
