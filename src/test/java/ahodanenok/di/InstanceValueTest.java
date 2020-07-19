package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;
import org.junit.jupiter.api.Test;

import javax.inject.Singleton;

import static org.junit.jupiter.api.Assertions.*;

public class InstanceValueTest {

    @Test
    public void test_1() {
        DependencyInstanceValue<String> v = new DependencyInstanceValue<>("test");
        assertEquals(String.class, v.id().type());
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
        assertEquals("test", v.provider().get());
    }

    @Test
    public void test_2() {
        DependencyInstanceValue<Number> v = new DependencyInstanceValue<Number>(DependencyIdentifier.of(Number.class), 10);
        assertEquals(Number.class, v.id().type());
        assertEquals(ScopeIdentifier.of(Singleton.class), v.scope());
        assertEquals(10, v.provider().get());
    }

    @Test
    public void test_3() {
        assertThrows(IllegalArgumentException.class, () -> new DependencyInstanceValue<>(null));
    }
}
