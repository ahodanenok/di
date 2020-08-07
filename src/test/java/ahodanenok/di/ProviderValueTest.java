package ahodanenok.di;

import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.value.ProviderValue;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import javax.inject.Singleton;

import static org.junit.jupiter.api.Assertions.*;

public class ProviderValueTest {

    @Test
    public void test_1() {
        ProviderValue<String> v = new ProviderValue<>(String.class, () -> "test");
        v.bind(DIContainer.builder().build().getContext());
        assertEquals(String.class, v.id().type());
        assertEquals(ScopeIdentifier.SINGLETON, v.metadata().scope());
        assertEquals("test", v.provider().get());
    }


    @Test
    public void test_2() {
        ProviderValue<String> v = new ProviderValue<>(String.class, TestProvider.class);
        v.bind(DIContainer.builder().build().getContext());
        assertEquals(String.class, v.id().type());
        assertEquals(ScopeIdentifier.NOT_SCOPED, v.metadata().scope());
        assertEquals("provider", v.provider().get());
    }
}

class TestProvider implements Provider<String> {

    public TestProvider() { }

    @Override
    public String get() {
        return "provider";
    }
}