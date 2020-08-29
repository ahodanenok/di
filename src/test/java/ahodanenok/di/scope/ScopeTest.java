package ahodanenok.di.scope;

import ahodanenok.di.DIContainer;
import ahodanenok.di.exception.ScopeResolutionException;

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

        ProviderValue<Test> v = new ProviderValue<>(Test.class, Test::new);

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

        ProviderValue<Test> v = new ProviderValue<>(Test.class, Test::new);

        SingletonScope scope = new SingletonScope();
        Test a = scope.get(v);
        Test b = scope.get(v);
        assertSame(a, b);
    }

    @Test
    public void testClassScopeResolutionNoScopeDeclared() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedClass.class));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(NotScopedClass.class, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testClassScopeResolutionNotScoped() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedByAnnotation.class));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(NotScopedByAnnotation.class, ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testClassScopeResolutionSingleton() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(SingletonClass.class));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(SingletonClass.class, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testMethodScopeResolutionSingleton() throws Exception {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(SingletonMethod.class.getDeclaredMethod("method")));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(
                        SingletonMethod.class.getDeclaredMethod("method"),
                        ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testMethodScopeResolutionNotScoped() throws Exception {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedMethod.class.getDeclaredMethod("method")));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(
                        NotScopedMethod.class.getDeclaredMethod("method"),
                        ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testMethodMultipleScopesFromStereotypes() throws Exception {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        Method m = MultipleScopedMethod.class.getDeclaredMethod("multipleScopesFromStereotypes");
        assertThrows(ScopeResolutionException.class, () -> resolution.resolve(m, ScopeIdentifier.NOT_SCOPED));
    }

    @Test
    public void testClassScopeResolutionInherited() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertEquals(ScopeIdentifier.of(S1.class), resolution.resolve(ClassWithInheritedScope.class));
        assertEquals(ScopeIdentifier.of(S1.class),
                resolution.resolve(ClassWithInheritedScope.class, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testClassScopeResolutionErrorIfMultipleScopesDeclared() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertThrows(ScopeResolutionException.class, () -> resolution.resolve(ClassWithMultipleInheritedScopes.class));
    }

    @Test
    public void testClassScopeResolutionIgnoringNotInheritedScope() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(ClassIgnoringNotInheritedScope.class));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(ClassIgnoringNotInheritedScope.class, ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testClassWithScopedStereotype() {
        AnnotatedScopeResolution scopeResolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        ScopeIdentifier scope = scopeResolution.resolve(
                ClassWithScopedStereotype.class,
                ScopeIdentifier.of(NotScoped.class));
        assertEquals(ScopeIdentifier.of(S1.class), scope);
    }

    @Test
    public void testClassScopedClassWithScopedStereotype() {
        AnnotatedScopeResolution scopeResolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        ScopeIdentifier scope = scopeResolution.resolve(
                ScopedClassWithScopedStereotype.class,
                ScopeIdentifier.of(NotScoped.class));
        assertEquals(ScopeIdentifier.of(Singleton.class), scope);
    }

    @Test
    public void testClassWithMultipleScopedStereotypes() {
        AnnotatedScopeResolution scopeResolution = new AnnotatedScopeResolution(DIContainer.builder().build());
        assertThrows(IllegalStateException.class, () -> scopeResolution.resolve(
                ClassWithMultipleScopedStereotypes.class,
                ScopeIdentifier.of(NotScoped.class)));
    }
}
