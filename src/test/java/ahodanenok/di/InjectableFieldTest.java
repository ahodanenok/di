package ahodanenok.di;

import ahodanenok.di.cl.AccessTest;
import ahodanenok.di.exception.InjectionFailedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InjectableFieldTest {

    @Test
    public void testPrivateAccess_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "private"))
                .build();

        AccessTest test = (AccessTest) new InjectableField(
                container, AccessTest.class.getDeclaredField("privateField")).inject(new AccessTest());
        assertEquals("private", test.getPrivateField());
        assertNull(test.getPublicField());
        assertNull(test.getPackageField());
        assertNull(test.getProtectedField());
    }

    @Test
    public void testPublicAccess_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "public"))
                .build();

        AccessTest test = (AccessTest) new InjectableField(
                container, AccessTest.class.getDeclaredField("publicField")).inject(new AccessTest());
        assertEquals("public", test.getPublicField());
        assertNull(test.getPrivateField());
        assertNull(test.getPackageField());
        assertNull(test.getProtectedField());
    }

    @Test
    public void testPackageAccess_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "default"))
                .build();

        AccessTest test = (AccessTest) new InjectableField(
                container, AccessTest.class.getDeclaredField("packageField")).inject(new AccessTest());
        assertEquals("default", test.getPackageField());
        assertNull(test.getPrivateField());
        assertNull(test.getPublicField());
        assertNull(test.getProtectedField());
    }

    @Test
    public void testProtectedAccess_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "protected"))
                .build();

        AccessTest test = (AccessTest) new InjectableField(
                container, AccessTest.class.getDeclaredField("protectedField")).inject(new AccessTest());
        assertEquals("protected", test.getProtectedField());
        assertNull(test.getPrivateField());
        assertNull(test.getPublicField());
        assertNull(test.getPackageField());
    }

    @Test
    public void testFinal_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "final"))
                .build();

        assertThrows(InjectionFailedException.class, () ->
                new InjectableField(container, AccessTest.class.getDeclaredField("finalField"))
                        .inject(new AccessTest()));
    }


    @Test
    public void testStatic_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "static"))
                .build();

        new InjectableField(
                container, AccessTest.class.getDeclaredField("staticField")).inject(null);
        assertEquals("static", AccessTest.getStaticField());
    }
}

