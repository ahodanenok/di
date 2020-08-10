package ahodanenok.di;

import ahodanenok.di.cl.AccessTest;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.value.InstanceValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InjectableMethodTest {

    @Test
    public void testPublic_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertEquals("public", new InjectableMethod(
                container, AccessTest.class.getDeclaredMethod("publicMethod")).inject(new AccessTest()));
    }


    @Test
    public void testPackage_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertEquals("package", new InjectableMethod(
                container, AccessTest.class.getDeclaredMethod("packageMethod")).inject(new AccessTest()));
    }

    @Test
    public void testProtected_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertEquals("protected", new InjectableMethod(
                container, AccessTest.class.getDeclaredMethod("protectedMethod")).inject(new AccessTest()));
    }

    @Test
    public void testPrivate_1() throws Exception {
        DIContainer container = DIContainer.builder().build();

        assertEquals("private", new InjectableMethod(
                container, AccessTest.class.getDeclaredMethod("privateMethod")).inject(new AccessTest()));
    }

    @Test
    public void testInject_1() throws Exception {
        class TestClass {
            int m(int n) {
                return n;
            }
        }

        DIContainer container = DIContainer.builder().addValue(new InstanceValue<>(int.class, 10)).build();
        assertEquals(10, new InjectableMethod(
                container, TestClass.class.getDeclaredMethod("m", int.class)).inject(new TestClass()));
    }


    @Test
    public void testInject_2() {
        class TestClass {
            int m(String n) {
                return 0;
            }
        }

        DIContainer container = DIContainer.builder().addValue(new InstanceValue<Object>(10)).build();
        assertThrows(UnsatisfiedDependencyException.class, () -> new InjectableMethod(
                container, TestClass.class.getDeclaredMethod("m", String.class)).inject(new TestClass()));
    }
}
