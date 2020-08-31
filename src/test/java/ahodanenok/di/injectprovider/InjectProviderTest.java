package ahodanenok.di.injectprovider;

import ahodanenok.di.DIContainer;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.injectprovider.classes.InjectingLaterProvider;
import ahodanenok.di.injectprovider.classes.InjectingProvider;
import ahodanenok.di.value.InstanceValue;
import ahodanenok.di.value.InstantiatingValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InjectProviderTest {

    @Test
    public void testProviderInjected() {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(InjectingProvider.class))
                .addValue(new InstanceValue<>("5"))
                .build();

        InjectingProvider p = container.instance(InjectingProvider.class);
        assertEquals("5", p.provider.get());
    }

    @Test
    public void testProviderNotFound() {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(InjectingProvider.class))
                .build();

        assertThrows(UnsatisfiedDependencyException.class, () -> container.instance(InjectingProvider.class));
    }

    @Test
    public void testLaterProviderInjected() {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(InjectingLaterProvider.class))
                .addValue(new InstanceValue<>("5"))
                .build();

        InjectingLaterProvider p = container.instance(InjectingLaterProvider.class);
        assertEquals("5", p.provider.get());
    }

    @Test
    public void testLaterProviderResolutionError() {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(InjectingLaterProvider.class))
                .build();

        InjectingLaterProvider p = container.instance(InjectingLaterProvider.class);
        assertNotNull(p.provider);
        assertThrows(UnsatisfiedDependencyException.class, () -> p.provider.get());
    }
}
