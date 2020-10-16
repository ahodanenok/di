package ahodanenok.di.scope;

import ahodanenok.di.DIContainer;
import ahodanenok.di.exception.ConfigurationException;

import static org.junit.jupiter.api.Assertions.*;

import ahodanenok.di.scope.classes.*;
import ahodanenok.di.value.ProviderValue;
import org.junit.jupiter.api.Test;

import javax.inject.Singleton;
import java.lang.reflect.Method;

// todo: test resolution for method and field
public class ScopeTest {

    @Test
    public void testScopeIdsMatchWhenCreatedFromClass() {
        assertEquals(ScopeIdentifier.of(NotScoped.class), ScopeIdentifier.of(NotScoped.class));
        assertEquals(ScopeIdentifier.of(Singleton.class), ScopeIdentifier.of(Singleton.class));
        assertNotEquals(ScopeIdentifier.of(Singleton.class), ScopeIdentifier.of(NotScoped.class));
    }

    @Test
    public void testScopeIdsMatchWhenCreatedFromAnnotations() {
        @Singleton
        @NotScoped
        class Test { }

        assertEquals(ScopeIdentifier.of(NotScoped.class), ScopeIdentifier.of(Test.class.getAnnotation(NotScoped.class)));
        assertEquals(ScopeIdentifier.of(Test.class.getAnnotation(NotScoped.class)), ScopeIdentifier.of(Test.class.getAnnotation(NotScoped.class)));
        assertEquals(ScopeIdentifier.of(Singleton.class), ScopeIdentifier.of(Test.class.getAnnotation(Singleton.class)));
        assertEquals(ScopeIdentifier.of(Test.class.getAnnotation(Singleton.class)), ScopeIdentifier.of(Test.class.getAnnotation(Singleton.class)));
        assertNotEquals(Test.class.getAnnotation(Singleton.class), Test.class.getAnnotation(NotScoped.class));
    }

    @Test
    public void testNotScopedReturnsCorrentId() {
        DefaultScope scope = new DefaultScope();
        assertEquals(ScopeIdentifier.of(NotScoped.class), scope.id());
    }

    @Test
    public void testNotScopedReturnsNewInstanceEveryTime() {
        class Test { }

        ProviderValue<Test> v = new ProviderValue<>(Test.class, () -> new Test());

        DefaultScope scope = new DefaultScope();
        Test a = scope.get(v);
        Test b = scope.get(v);
        assertNotSame(a, b);
    }

    @Test
    public void testSingletonReturnsCorrentId() {
        SingletonScope scope = new SingletonScope();
        assertEquals(ScopeIdentifier.of(Singleton.class), scope.id());
    }

    @Test
    public void testSingletonReturnsSameInstanceEveryTime() {
        class Test { }

        ProviderValue<Test> v = new ProviderValue<>(Test.class, () -> new Test());

        SingletonScope scope = new SingletonScope();
        Test a = scope.get(v);
        Test b = scope.get(v);
        assertSame(a, b);
    }

    @Test
    public void testClassScopeResolutionNoScopeDeclared() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedClass.class, ScopeIdentifier.NOT_SCOPED));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(NotScopedClass.class, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testClassScopeResolutionNotScoped() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedByAnnotation.class, ScopeIdentifier.NOT_SCOPED));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(NotScopedByAnnotation.class, ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testClassScopeResolutionSingleton() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(SingletonClass.class, ScopeIdentifier.NOT_SCOPED));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(SingletonClass.class, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testMethodScopeResolutionSingleton() throws Exception {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(SingletonMethod.class.getDeclaredMethod("method"), ScopeIdentifier.NOT_SCOPED));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(
                        SingletonMethod.class.getDeclaredMethod("method"),
                        ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testMethodScopeResolutionNotScoped() throws Exception {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedMethod.class.getDeclaredMethod("method"), ScopeIdentifier.NOT_SCOPED));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(
                        NotScopedMethod.class.getDeclaredMethod("method"),
                        ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testMethodMultipleScopesFromStereotypes() throws Exception {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        Method m = MultipleScopedMethod.class.getDeclaredMethod("multipleScopesFromStereotypes");
        assertThrows(ConfigurationException.class, () -> resolution.resolve(m, ScopeIdentifier.NOT_SCOPED));
    }

    @Test
    public void testClassScopeResolutionInherited() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertEquals(ScopeIdentifier.of(S1.class), resolution.resolve(ClassWithInheritedScope.class, ScopeIdentifier.NOT_SCOPED));
        assertEquals(ScopeIdentifier.of(S1.class),
                resolution.resolve(ClassWithInheritedScope.class, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testClassScopeResolutionErrorIfMultipleScopesDeclared() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertThrows(ConfigurationException.class, () -> resolution.resolve(ClassWithMultipleInheritedScopes.class, ScopeIdentifier.NOT_SCOPED));
    }

    @Test
    public void testClassScopeResolutionIgnoringNotInheritedScope() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(ClassIgnoringNotInheritedScope.class, ScopeIdentifier.NOT_SCOPED));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(ClassIgnoringNotInheritedScope.class, ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testClassWithScopedStereotype() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        ScopeIdentifier scope = resolution.resolve(
                ClassWithScopedStereotype.class,
                ScopeIdentifier.of(NotScoped.class));
        assertEquals(ScopeIdentifier.of(S1.class), scope);
    }

    @Test
    public void testClassScopedClassWithScopedStereotype() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        ScopeIdentifier scope = resolution.resolve(
                ScopedClassWithScopedStereotype.class,
                ScopeIdentifier.of(NotScoped.class));
        assertEquals(ScopeIdentifier.of(Singleton.class), scope);
    }

    @Test
    public void testClassWithMultipleScopedStereotypes() {
        DIContainer container = DIContainer.builder().build();
        ScopeResolution resolution = container.instance(ScopeResolution.class);
        assertThrows(ConfigurationException.class, () -> resolution.resolve(
                ClassWithMultipleScopedStereotypes.class,
                ScopeIdentifier.of(NotScoped.class)));
    }
}
