package ahodanenok.di;

import ahodanenok.di.cl.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InjectableConstructorTest {

    @Test
    public void testPublic_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertNotNull(new InjectableConstructor<>(
                container, PublicConstructor.class.getDeclaredConstructor()).inject());
    }


    @Test
    public void testPackage_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertNotNull(new InjectableConstructor<>(
                container, PackageConstructor.class.getDeclaredConstructor()).inject());
    }

    @Test
    public void testProtected_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertNotNull(new InjectableConstructor<>(
                container, ProtectedConstructor.class.getDeclaredConstructor()).inject());
    }

    @Test
    public void testPrivate_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertNotNull(new InjectableConstructor<>(
                container, PrivateConstructor.class.getDeclaredConstructor()).inject());
    }

    @Test
    public void testInject_1() throws Exception {
        class TestClass {
            int n;

            TestClass(int n) {
                this.n = n;
            }
        }

        DIContainer container = DIContainer.builder().addValue(new DependencyInstanceValue<Object>(10)).build();
        TestClass t = new InjectableConstructor<>(container, TestClass.class.getDeclaredConstructor(InjectableConstructorTest.class)).inject();
        assertEquals(10, t.n);
    }


    @Test
    public void testInject_2() {
        class TestClass {
            TestClass(String n) { }
        }

        DIContainer container = DIContainer.builder().addValue(new DependencyInstanceValue<Object>(10)).build();
        assertThrows(RuntimeException.class, () ->
                new InjectableConstructor<>(container, TestClass.class.getDeclaredConstructor(InjectableConstructorTest.class)).inject());
    }
}
