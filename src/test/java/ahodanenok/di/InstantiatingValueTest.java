package ahodanenok.di;

import ahodanenok.di.exception.DependencyInjectionException;
import ahodanenok.di.exception.DependencyInstantiatingException;
import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.value.InstanceValue;
import ahodanenok.di.value.InstantiatingValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
                .addValue(new InstanceValue<>(10))
                .build();
    }

    @Test
    public void testInstanceClass_1() {
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(TestInterface.class));
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(TestInterface.class, TestInterface.class));
    }

    @Test
    public void testInstanceClass_2() {
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(int.class));
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(int.class, int.class));
    }

    @Test
    public void testInstanceClass_3() {
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(byte[].class));
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(byte[].class, byte[].class));
    }

    @Test
    public void testInstanceClass_4() {
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(Qualifier.class));
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(Qualifier.class, Qualifier.class));
    }

    enum TestEnum { }
    @Test
    public void testInstanceClass_5() {
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(TestEnum.class));
        assertThrows(IllegalArgumentException.class, () -> new InstantiatingValue<>(TestEnum.class, TestEnum.class));
    }

    @Test
    public void testQualifiers_1() {
        InstantiatingValue<TestInstance> v =
                new InstantiatingValue<>(TestInstance.class);
        v.bind(container.getContext());
        assertEquals(1, v.id().qualifiers().size());
        assertEquals(TestQualifier_Class.class, v.id().qualifiers().iterator().next().annotationType());
        assertEquals(ScopeIdentifier.NOT_SCOPED, v.metadata().scope());
    }

    @Test
    public void testQualifiers_2() {
        InstantiatingValue<TestInterface> v =
                new InstantiatingValue<>(TestInterface.class, TestInstance.class);
        v.bind(container.getContext());
        assertEquals(1, v.id().qualifiers().size());
        assertEquals(TestQualifier_Class.class, v.id().qualifiers().iterator().next().annotationType());
        assertEquals(ScopeIdentifier.NOT_SCOPED, v.metadata().scope());
    }

    @Test
    public void testQualifiers_3() {
        InstantiatingValue<TestInstance> v =
                new InstantiatingValue<>(TestInstance.class, TestInstance.class);
        v.bind(container.getContext());
        assertEquals(1, v.id().qualifiers().size());
        assertEquals(TestQualifier_Class.class, v.id().qualifiers().iterator().next().annotationType());
        assertEquals(ScopeIdentifier.NOT_SCOPED, v.metadata().scope());
    }

    @Test
    public void testScope_1() {
        InstantiatingValue<TestInstanceScope> v =
                new InstantiatingValue<>(TestInstanceScope.class);
        v.bind(container.getContext());
        assertEquals(ScopeIdentifier.SINGLETON, v.metadata().scope());
    }

    @Test
    public void testScope_2() {
        InstantiatingValue<TestInterface> v =
                new InstantiatingValue<>(TestInterface.class, TestInstanceScope.class);
        v.bind(container.getContext());
        assertEquals(ScopeIdentifier.SINGLETON, v.metadata().scope());
    }

    @Test
    public void testScope_3() {
        InstantiatingValue<TestInstanceScope> v =
                new InstantiatingValue<>(TestInstanceScope.class, TestInstanceScope.class);
        v.bind(container.getContext());
        assertEquals(ScopeIdentifier.SINGLETON, v.metadata().scope());
    }

    @Test
    public void testDefaultConstructor_1() {
        InstantiatingValue<TestClass_Default> v =
                new InstantiatingValue<>(TestClass_Default.class);
        v.bind(container.getContext());

        assertNotNull(v.provider().get());
    }

    @Test
    @Disabled // todo: exception
    public void testDefaultConstructor_2() {
        InstantiatingValue<TestClass_Default_NotPublic> v =
                new InstantiatingValue<>(TestClass_Default_NotPublic.class);
        v.bind(container.getContext());

        assertThrows(DependencyInjectionException.class, () -> v.provider().get());
    }

    @Test
    @Disabled // todo: exception
    public void testDefaultConstructor_3() {
        InstantiatingValue<TestClass_Default_NotSingle> v =
                new InstantiatingValue<>(TestClass_Default_NotSingle.class);
        v.bind(container.getContext());

        assertThrows(DependencyInjectionException.class, () -> v.provider().get());
    }

    @Test
    @Disabled // todo: exception
    public void testMultipleConstructors_1() {
        InstantiatingValue<TestClass_Multiple> v =
                new InstantiatingValue<>(TestClass_Multiple.class);
        v.bind(container.getContext());

        assertThrows(DependencyInstantiatingException.class, () -> v.provider().get());
    }


    @Test
    public void testMultipleConstructors_2() {
        InstantiatingValue<TestInject_MultipleInject> v =
                new InstantiatingValue<>(TestInject_MultipleInject.class);
        v.bind(container.getContext());

       assertEquals(10, v.provider().get().n);
    }

    public static class NestedClass { }

    @Test
    public void testNestedClass_1() {
        InstantiatingValue<NestedClass> v =
                new InstantiatingValue<>(NestedClass.class);
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
