package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.value.InstanceValue;
import org.junit.jupiter.api.Test;

import javax.inject.Singleton;

import static org.junit.jupiter.api.Assertions.*;

public class InstanceValueTest {

    @Test
    public void test_1() {
        InstanceValue<String> v = new InstanceValue<>("test");
        assertEquals(String.class, v.id().type());
        assertEquals(String.class, v.metadata().valueType());
        assertEquals(ScopeIdentifier.SINGLETON, v.metadata().scope());
        assertEquals("test", v.provider().get());
    }

    @Test
    public void test_2() {
        InstanceValue<Number> v = new InstanceValue<Number>(Number.class, 10);
        assertEquals(Number.class, v.id().type());
        assertEquals(Integer.class, v.metadata().valueType());
        assertEquals(ScopeIdentifier.SINGLETON, v.metadata().scope());
        assertEquals(10, v.provider().get());
    }

    @Test
    public void test_3() {
        assertThrows(NullPointerException.class, () -> new InstanceValue<>(null));
    }
}
