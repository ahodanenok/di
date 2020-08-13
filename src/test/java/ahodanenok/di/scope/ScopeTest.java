package ahodanenok.di.scope;

import ahodanenok.di.exception.ScopeResolutionException;

import static org.junit.jupiter.api.Assertions.*;

import ahodanenok.di.scope.classes.*;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.value.ProviderValue;
import org.junit.jupiter.api.Test;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

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
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedClass.class));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(NotScopedClass.class, Collections::emptySet, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testClassScopeResolutionNotScoped() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedByAnnotation.class));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(NotScopedByAnnotation.class, Collections::emptySet, ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testClassScopeResolutionSingleton() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(SingletonClass.class));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(SingletonClass.class, Collections::emptySet, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testMethodScopeResolutionSingleton() throws Exception {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(SingletonMethod.class.getDeclaredMethod("method")));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(
                        SingletonMethod.class.getDeclaredMethod("method"),
                        Collections::emptySet,
                        ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testMethodScopeResolutionNotScoped() throws Exception {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(NotScopedMethod.class.getDeclaredMethod("method")));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(
                        NotScopedMethod.class.getDeclaredMethod("method"),
                        Collections::emptySet,
                        ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testMethodMultipleScopesFromStereotypes() throws Exception {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        AnnotatedStereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();
        Method m = MultipleScopedMethod.class.getDeclaredMethod("multipleScopesFromStereotypes");
        assertThrows(ScopeResolutionException.class,
                () -> resolution.resolve(m, () -> stereotypeResolution.resolve(m), ScopeIdentifier.NOT_SCOPED));
    }

    @Test
    public void testClassScopeResolutionInherited() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(S1.class), resolution.resolve(ClassWithInheritedScope.class));
        assertEquals(ScopeIdentifier.of(S1.class),
                resolution.resolve(ClassWithInheritedScope.class, Collections::emptySet, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testClassScopeResolutionErrorIfMultipleScopesDeclared() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertThrows(ScopeResolutionException.class, () -> resolution.resolve(ClassWithMultipleInheritedScopes.class));
    }

    @Test
    public void testClassScopeResolutionIgnoringNotInheritedScope() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(ClassIgnoringNotInheritedScope.class));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(ClassIgnoringNotInheritedScope.class, Collections::emptySet, ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testClassWithScopedStereotype() {
        AnnotatedScopeResolution scopeResolution = new AnnotatedScopeResolution();
        AnnotatedStereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        ScopeIdentifier scope = scopeResolution.resolve(
                ClassWithScopedStereotype.class,
                () -> stereotypeResolution.resolve(ClassWithScopedStereotype.class),
                ScopeIdentifier.of(NotScoped.class));
        assertEquals(ScopeIdentifier.of(S1.class), scope);
    }

    @Test
    public void testClassScopedClassWithScopedStereotype() {
        AnnotatedScopeResolution scopeResolution = new AnnotatedScopeResolution();
        AnnotatedStereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        ScopeIdentifier scope = scopeResolution.resolve(
                ScopedClassWithScopedStereotype.class,
                () -> stereotypeResolution.resolve(ScopedClassWithScopedStereotype.class),
                ScopeIdentifier.of(NotScoped.class));
        assertEquals(ScopeIdentifier.of(Singleton.class), scope);
    }

    @Test
    public void testClassWithMultipleScopedStereotypes() {
        AnnotatedScopeResolution scopeResolution = new AnnotatedScopeResolution();
        AnnotatedStereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        assertThrows(IllegalStateException.class, () -> scopeResolution.resolve(
                ClassWithMultipleScopedStereotypes.class,
                () -> stereotypeResolution.resolve(ClassWithMultipleScopedStereotypes.class),
                ScopeIdentifier.of(NotScoped.class)));
    }
}
