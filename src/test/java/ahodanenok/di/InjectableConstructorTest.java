package ahodanenok.di;

import ahodanenok.di.cl.*;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InjectableConstructorTest {

    @Test
    public void testPublic_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertNotNull(new InjectableConstructor<>(
                container.getContext(), PublicConstructor.class.getDeclaredConstructor()).inject());
    }


    @Test
    public void testPackage_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertNotNull(new InjectableConstructor<>(
                container.getContext(), PackageConstructor.class.getDeclaredConstructor()).inject());
    }

    @Test
    public void testProtected_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertNotNull(new InjectableConstructor<>(
                container.getContext(), ProtectedConstructor.class.getDeclaredConstructor()).inject());
    }

    @Test
    public void testPrivate_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertNotNull(new InjectableConstructor<>(
                container.getContext(), PrivateConstructor.class.getDeclaredConstructor()).inject());
    }

    @Test
    public void testInject_1() throws Exception {
        class TestClass {
            int n;

            TestClass(int n) {
                this.n = n;
            }
        }

        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(this))
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(int.class), 10))
                .build();
        TestClass t = new InjectableConstructor<>(container.getContext(), TestClass.class.getDeclaredConstructor(InjectableConstructorTest.class, int.class)).inject();
        assertEquals(10, t.n);
    }


    @Test
    public void testInject_2() {
        class TestClass {
            TestClass(String n) { }
        }

        DIContainer container = DIContainer.builder().addValue(new DependencyInstanceValue<Object>(10)).build();
        assertThrows(UnsatisfiedDependencyException.class, () ->
                new InjectableConstructor<>(container.getContext(), TestClass.class.getDeclaredConstructor(InjectableConstructorTest.class, String.class)).inject());
    }
}
