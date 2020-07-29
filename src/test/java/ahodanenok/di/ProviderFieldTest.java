package ahodanenok.di;

import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;
import com.sun.source.tree.Scope;
import org.junit.jupiter.api.Assertions;
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
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(String.class, PF1.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new DependencyInstantiatingValue<>(PF1.class)).build());

        assertEquals(String.class, v.id().type());
        assertEquals(Collections.emptySet(), v.id().qualifiers());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
        assertEquals("1", v.provider().get());
    }

    @Test
    public void test_2() throws Exception {
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(DependencyIdentifier.of(String.class), PF1.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new DependencyInstantiatingValue<>(PF1.class)).build());

        assertEquals(String.class, v.id().type());
        assertEquals(Collections.emptySet(), v.id().qualifiers());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
        assertEquals("1", v.provider().get());
    }

    @Test
    public void test_3() throws Exception {
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(String.class, PF2.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new DependencyInstantiatingValue<>(PF2.class)).build());

        assertEquals(String.class, v.id().type());
        assertEquals(Collections.singleton(PF2.class.getDeclaredField("f").getDeclaredAnnotation(A.class)), v.id().qualifiers());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
        assertEquals("2", v.provider().get());
    }

    @Test
    public void test_4() throws Exception {
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(DependencyIdentifier.of(String.class), PF2.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new DependencyInstantiatingValue<>(PF2.class)).build());

        assertEquals(String.class, v.id().type());
        assertEquals(Collections.emptySet(), v.id().qualifiers());
        assertEquals(ScopeIdentifier.of(NotScoped.class), v.scope());
        assertEquals("2", v.provider().get());
    }

    @Test
    public void test_5() throws Exception {
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(String.class, PF3.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new DependencyInstantiatingValue<>(PF3.class)).build());

        assertEquals(String.class, v.id().type());
        assertEquals(Collections.singleton(PF3.class.getDeclaredField("f").getDeclaredAnnotation(A.class)), v.id().qualifiers());
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
        assertEquals("3", v.provider().get());
    }

    @Test
    public void test_6() throws Exception {
        DependencyFieldProviderValue<String> v = new DependencyFieldProviderValue<>(DependencyIdentifier.of(String.class), PF3.class.getDeclaredField("f"));
        v.bind(DIContainer.builder().addValue(new DependencyInstantiatingValue<>(PF3.class)).build());

        assertEquals(String.class, v.id().type());
        assertEquals(Collections.emptySet(), v.id().qualifiers());
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
        assertEquals("3", v.provider().get());
    }

    @Test
    public void test_7() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            DependencyFieldProviderValue<Integer> v = new DependencyFieldProviderValue<>(DependencyIdentifier.of(int.class), PF3.class.getDeclaredField("f"));
        });
    }
}
