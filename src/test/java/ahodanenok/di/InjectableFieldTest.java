package ahodanenok.di;

import ahodanenok.di.cl.AccessTest;
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
        assertNull(test.getDefaultField());
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
        assertNull(test.getDefaultField());
        assertNull(test.getProtectedField());
    }

    @Test
    public void testPackageAccess_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "default"))
                .build();

        AccessTest test = (AccessTest) new InjectableField(
                container, AccessTest.class.getDeclaredField("defaultField")).inject(new AccessTest());
        assertEquals("default", test.getDefaultField());
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
                container, AccessTest.class.getDeclaredField("defaultField")).inject(new AccessTest());
        assertEquals("protected", test.getProtectedField());
        assertNull(test.getPrivateField());
        assertNull(test.getPublicField());
        assertNull(test.getDefaultField());
    }

    @Test
    public void testFinal_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "final"))
                .build();

        // todo: exception
        assertThrows(RuntimeException.class, () ->
                new InjectableField(container, AccessTest.class.getDeclaredField("finalField"))
                        .inject(new AccessTest()));
    }


    @Test
    public void testStatic_1() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class), "static"))
                .build();

        AccessTest test = (AccessTest) new InjectableField(
                container, AccessTest.class.getDeclaredField("staticField")).inject(null);
        assertEquals("test", AccessTest.getStaticField());
    }
}

