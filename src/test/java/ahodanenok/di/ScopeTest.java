package ahodanenok.di;

import ahodanenok.di.scope.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import javax.inject.Singleton;

public class ScopeTest {

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

        DependencyIdentifier<Test> id = DependencyIdentifier.of(Test.class);
        Provider<Test> provider = () -> new Test();

        DefaultScope scope = new DefaultScope();
        Test a = scope.get(id, provider);
        Test b = scope.get(id, provider);
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

        DependencyIdentifier<Test> id = DependencyIdentifier.of(Test.class);
        Provider<Test> provider = () -> new Test();

        SingletonScope scope = new SingletonScope();
        Test a = scope.get(id, provider);
        Test b = scope.get(id, provider);
        assertSame(a, b);
    }

    @Test
    public void testScopeResolution_1() {
        class Test { }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class));
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testScopeResolution_2() {
        @NotScoped
        class Test { }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class));
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class, ScopeIdentifier.of(Singleton.class)));
    }

    @Test
    public void testScopeResolution_3() {
        @Singleton
        class Test { }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(Test.class));
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(Test.class, ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testScopeResolution_4() throws Exception {
        @NotScoped
        class Test {

            @Singleton
            void method() { }
        }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(Test.class.getMethod("method")));
        assertEquals(ScopeIdentifier.of(Singleton.class), resolution.resolve(Test.class.getMethod("method"), ScopeIdentifier.of(NotScoped.class)));
    }

    @Test
    public void testScopeResolution_5() throws Exception {
        @Singleton
        class Test {

            @NotScoped
            void method() { }
        }

        AnnotatedScopeResolution resolution = new AnnotatedScopeResolution();
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class.getMethod("method")));
        assertEquals(ScopeIdentifier.of(NotScoped.class), resolution.resolve(Test.class.getMethod("method"), ScopeIdentifier.of(Singleton.class)));
    }
}
