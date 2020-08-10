package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.value.FieldProviderValue;
import ahodanenok.di.value.InstantiatingValue;
import org.junit.jupiter.api.Test;

import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ProviderFieldTest {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface A { }

    public static class PF1 {
        String f = "1";
    }

    public static class PF2 {
        @A String f = "2";
    }

    public static class PF3 {
        @A @Singleton String f = "3";
    }

    @Test
    public void test_1() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, PF1.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new InstantiatingValue<>(PF1.class)).build());

        assertEquals(String.class, v.type());
        assertEquals(Collections.emptySet(), v.metadata().getQualifiers());
        assertEquals(ScopeIdentifier.NOT_SCOPED, v.metadata().getScope());
        assertEquals("1", v.provider().get());
    }

    @Test
    public void test_2() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, PF1.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new InstantiatingValue<>(PF1.class)).build());

        assertEquals(String.class, v.type());
        assertEquals(Collections.emptySet(), v.metadata().getQualifiers());
        assertEquals(ScopeIdentifier.NOT_SCOPED, v.metadata().getScope());
        assertEquals("1", v.provider().get());
    }

    @Test
    public void test_3() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, PF2.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new InstantiatingValue<>(PF2.class)).build());

        assertEquals(String.class, v.type());
        assertEquals(Collections.singleton(PF2.class.getDeclaredField("f").getDeclaredAnnotation(A.class)), v.metadata().getQualifiers());
        assertEquals(ScopeIdentifier.NOT_SCOPED, v.metadata().getScope());
        assertEquals("2", v.provider().get());
    }

    @Test
    public void test_4() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, PF2.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new InstantiatingValue<>(PF2.class)).build());

        assertEquals(String.class, v.type());
        assertEquals(Collections.singleton(PF2.class.getDeclaredField("f").getAnnotation(A.class)), v.metadata().getQualifiers());
        assertEquals(ScopeIdentifier.NOT_SCOPED, v.metadata().getScope());
        assertEquals("2", v.provider().get());
    }

    @Test
    public void test_5() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, PF3.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new InstantiatingValue<>(PF3.class)).build());

        assertEquals(String.class, v.type());
        assertEquals(Collections.singleton(PF3.class.getDeclaredField("f").getDeclaredAnnotation(A.class)), v.metadata().getQualifiers());
        assertEquals(ScopeIdentifier.SINGLETON, v.metadata().getScope());
        assertEquals("3", v.provider().get());
    }

    @Test
    public void test_6() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(String.class, PF3.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new InstantiatingValue<>(PF3.class)).build());

        assertEquals(String.class, v.type());
        assertEquals(Collections.singleton(PF3.class.getDeclaredField("f").getDeclaredAnnotation(A.class)), v.metadata().getQualifiers());
        assertEquals(ScopeIdentifier.SINGLETON, v.metadata().getScope());
        assertEquals("3", v.provider().get());
    }

    @Test
    public void test_7() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            FieldProviderValue<Integer> v = new FieldProviderValue<>(int.class, PF3.class.getDeclaredField("f"));
        });
    }
}
