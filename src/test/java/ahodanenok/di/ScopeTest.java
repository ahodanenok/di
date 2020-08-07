package ahodanenok.di;

import ahodanenok.di.exception.ScopeResolutionException;
import ahodanenok.di.scope.*;

import static org.junit.jupiter.api.Assertions.*;

import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.Stereotype;
import ahodanenok.di.value.ProviderValue;
import org.junit.jupiter.api.Test;

import javax.inject.Scope;
import javax.inject.Singleton;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;

// todo: test resolution for method and field
public class ScopeTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Scope
    @interface S1 { }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Scope
    @interface S2 { }

    @Retention(RetentionPolicy.RUNTIME)
    @Scope
    @interface NonInheritedScope { }

    @S1
    class Parent { }
    class ChildA extends Parent { }

    @S1 @S2
    class ChildMultipleInheritedScopes extends Parent { }
    class ChildB extends ChildMultipleInheritedScopes { }

    @NonInheritedScope
    class ChildNonInheritedScope extends Parent { }
    class ChildC extends ChildNonInheritedScope { }

    @S1
    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface StereotypeWithScope1 { }

    @S2
    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface StereotypeWithScope2 { }

    @StereotypeWithScope1
    class ClassWithScopedStereotype { }

    @Singleton @StereotypeWithScope1
    class ScopedClassWithScopedStereotype { }

    @StereotypeWithScope1 @StereotypeWithScope2
    class ClassWithMultipleScopedStereotypes { }

    @Test
    public void testScopeId_1() {
        assertEquals(ScopeIdentifier.of(NotScoped.class), ScopeIdentifier.of(NotScoped.class));
        assertEquals(ScopeIdentifier.of(Singleton.class), ScopeIdentifier.of(Singleton.class));
        assertNotEquals(ScopeIdentifier.of(Singleton.class), ScopeIdentifier.of(NotScoped.class));
    }

    @Test
    public void testScopeId_2() {
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
    public void testNotScoped_1() {
        DefaultScope scope = new DefaultScope();
        assertEquals(ScopeIdentifier.of(NotScoped.class), scope.id());
    }

    @Test
    public void testNotScoped_2() {
        class Test { }

        ProviderValue<Test> v = new ProviderValue<>(Test.class, Test::new);

        DefaultScope scope = new DefaultScope();
        Test a = scope.get(v);
        Test b = scope.get(v);
        assertNotSame(a, b);
    }

    @Test
    public void testSingleton_1() {
        SingletonScope scope = new SingletonScope();
        assertEquals(ScopeIdentifier.of(Singleton.class), scope.id());
    }

    @Test
    public void testSingleton_2() {
        class Test { }

        ProviderValue<Test> v = new ProviderValue<>(Test.class, Test::new);

        SingletonScope scope = new SingletonScope();
        Test a = scope.get(v);
        Test b = scope.get(v);
        assertSame(a, b);
    }

    @Test
    public void testScopeResolution_1() {
        class Test { }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(Test.class, Collections::emptySet, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testScopeResolution_2() {
        @NotScoped
        class Test { }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(Test.class, Collections::emptySet, ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testScopeResolution_3() {
        @Singleton
        class Test { }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(Test.class));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(Test.class, Collections::emptySet, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testScopeResolution_4() throws Exception {
        @NotScoped
        class Test {

            @Singleton
            void method() { }
        }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(Test.class.getDeclaredMethod("method")));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(
                        Test.class.getDeclaredMethod("method"),
                        Collections::emptySet,
                        ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testScopeResolution_5() throws Exception {
        @Singleton
        class Test {

            @NotScoped
            void method() { }
        }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class.getDeclaredMethod("method")));
        assertEquals(ScopeIdentifier.of(NotScoped.class),
                resolution.resolve(
                        Test.class.getDeclaredMethod("method"),
                        Collections::emptySet,
                        ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testScopeResolution_6() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(S1.class), resolution.resolve(ChildA.class));
        assertEquals(ScopeIdentifier.of(S1.class),
                resolution.resolve(ChildA.class, Collections::emptySet, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testScopeResolution_7() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertThrows(ScopeResolutionException.class, () -> resolution.resolve(ChildB.class));
    }

    @Test
    public void testScopeResolution_8() {
        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(ChildC.class));
        assertEquals(ScopeIdentifier.of(Singleton.class),
                resolution.resolve(ChildC.class, Collections::emptySet, ScopeIdentifier.of(Singleton.class)));
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
    public void testScopedClassWithScopedStereotype() {
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
