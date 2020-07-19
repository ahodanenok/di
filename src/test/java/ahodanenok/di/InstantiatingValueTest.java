package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;
import org.junit.jupiter.api.Assertions;
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
        v.bind(container);
        assertNotNull(v.id().qualifier());
        assertEquals(TestQualifier_Class.class, v.id().qualifier().annotationType());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
    }

    @Test
    public void testQualifiers_2() {
        DependencyInstantiatingValue<TestInterface> v =
                new DependencyInstantiatingValue<>(TestInterface.class, TestInstance.class);
        System.out.println(container.scopeResolution());
        v.bind(container);
        assertNotNull(v.id().qualifier());
        assertEquals(TestQualifier_Class.class, v.id().qualifier().annotationType());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
    }

    @Test
    public void testQualifiers_3() {
        DependencyInstantiatingValue<TestInstance> v =
                new DependencyInstantiatingValue<>(DependencyIdentifier.of(TestInstance.class), TestInstance.class);
        v.bind(container);
        assertNull(v.id().qualifier());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
    }

    @Test
    public void testScope_1() {
        DependencyInstantiatingValue<TestInstanceScope> v =
                new DependencyInstantiatingValue<>(TestInstanceScope.class);
        v.bind(container);
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
    }

    @Test
    public void testScope_2() {
        DependencyInstantiatingValue<TestInterface> v =
                new DependencyInstantiatingValue<>(TestInterface.class, TestInstanceScope.class);
        v.bind(container);
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
    }

    @Test
    public void testScope_3() {
        DependencyInstantiatingValue<TestInstanceScope> v =
                new DependencyInstantiatingValue<>(DependencyIdentifier.of(TestInstanceScope.class), TestInstanceScope.class);
        v.bind(container);
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
    }

    @Test
    public void testInject_1() {
        DependencyInstantiatingValue<TestInterface> v =
                new DependencyInstantiatingValue<>(TestInterface.class, TestInject.class);
        v.bind(container);
        assertEquals(10, ((TestInject) v.provider().get()).n);
    }

    @Test
    public void testMultipleConstructors_1() {
        DependencyInstantiatingValue<TestClass_Multiple> v =
                new DependencyInstantiatingValue<>(TestClass_Multiple.class);
        v.bind(container);

        assertThrows(InjectionFailedException.class, () -> v.provider().get());
    }


    @Test
    public void testMultipleConstructors_2() {
        DependencyInstantiatingValue<TestInject_Single> v =
                new DependencyInstantiatingValue<>(TestInject_Single.class);
        v.bind(container);

       assertEquals(10, v.provider().get().n);
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

class TestInject implements TestInterface {
    @Inject int n;
}

class TestClass_Multiple {
    TestClass_Multiple(String n) { }
    TestClass_Multiple(int a) { }
}

class TestInject_Single {
    int n;

    TestInject_Single(String n) { }
    @Inject TestInject_Single(int a) {
        this.n = a;
    }
}


