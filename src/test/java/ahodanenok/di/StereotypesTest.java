package ahodanenok.di;

import ahodanenok.di.stereotype.DefaultStereotypeResolution;
import ahodanenok.di.stereotype.Stereotype;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class StereotypesTest {

    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface A { }

    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface B { }

    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface C { }

    @M2
    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface M1 { }

    @M3
    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface M2 { }

    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface M3 { }

    @Stereotype
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @interface P { }

    @Loop2
    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface Loop1 { }

    @Loop1
    @Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @interface Loop2 { }


    public static class ClassNoStereotypes { }

    @A
    public static class ClassSingleStereotype { }

    @P @C
    public static class ParentWithInheritedStereotype { }

    @A @B
    public static class ClassInheritedStereotype extends ParentWithInheritedStereotype { }

    @M1
    public static class ClassWithStereotypesWithStereotypes { }

    @Loop1
    public static class ClassWithStereotypeLoop { }

    @Test
    public void testClassNoStereotypes() {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        assertTrue(resolution.resolve(ClassNoStereotypes.class).isEmpty());
    }

    @Test
    public void testClassSingleStereotype() {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Annotation> s = resolution.resolve(ClassSingleStereotype.class);
        assertEquals(1, s.size());
        assertEquals(A.class, s.iterator().next().annotationType());
    }

    @Test
    public void testClassWithInheritedStereotype() {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(ClassInheritedStereotype.class)
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());
        assertEquals(3, s.size());
        assertTrue(s.containsAll(Arrays.asList(A.class, B.class, P.class)));
    }

    @Test
    public void testClassWithStereotypeWithStereotypes() {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(ClassWithStereotypesWithStereotypes.class)
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(3, s.size());
        assertTrue(s.containsAll(Arrays.asList(M1.class, M2.class, M3.class)));
    }

    @Test
    public void testClassStereotypeLoop() {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(ClassWithStereotypeLoop.class)
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(2, s.size());
        assertTrue(s.containsAll(Arrays.asList(Loop1.class, Loop2.class)));
    }

    public static class FieldNoStereotypes {
        String f;
    }

    public static class FieldSingleStereotype {
        @A String f;
    }

    public static class FieldWithStereotypesWithStereotypes {
        @M1 String f;
    }

    public static class FieldWithStereotypesLoop {
        @Loop1 String f;
    }

    @Test
    public void testFieldNoStereotypes() throws Exception {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        assertTrue(resolution.resolve(FieldNoStereotypes.class.getDeclaredField("f")).isEmpty());
    }

    @Test
    public void testFieldSingleStereotype() throws Exception {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Annotation> s = resolution.resolve(FieldSingleStereotype.class.getDeclaredField("f"));
        assertEquals(1, s.size());
        assertEquals(A.class, s.iterator().next().annotationType());
    }

    @Test
    public void testFieldWithStereotypeWithStereotypes() throws Exception {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(FieldWithStereotypesWithStereotypes.class.getDeclaredField("f"))
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(3, s.size());
        assertTrue(s.containsAll(Arrays.asList(M1.class, M2.class, M3.class)));
    }

    @Test
    public void testFieldWithStereotypesLoop() throws Exception {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(FieldWithStereotypesLoop.class.getDeclaredField("f"))
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(2, s.size());
        assertTrue(s.containsAll(Arrays.asList(Loop1.class, Loop2.class)));
    }

    public static class MethodNoStereotypes {
        void m() { }
    }

    public static class MethodSingleStereotype {
        @A void m() { }
    }

    public static class MethodWithStereotypesWithStereotypes {
        @M1 void m() { }
    }

    public static class MethodWithStereotypesLoop {
        @Loop1 void m() { }
    }

    @Test
    public void testMethodNoStereotypes() throws Exception {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        assertTrue(resolution.resolve(MethodNoStereotypes.class.getDeclaredMethod("m")).isEmpty());
    }

    @Test
    public void testMethodSingleStereotype() throws Exception {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Annotation> s = resolution.resolve(MethodSingleStereotype.class.getDeclaredMethod("m"));
        assertEquals(1, s.size());
        assertEquals(A.class, s.iterator().next().annotationType());
    }

    @Test
    public void testMethodWithStereotypeWithStereotypes() throws Exception {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(MethodWithStereotypesWithStereotypes.class.getDeclaredMethod("m"))
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(3, s.size());
        assertTrue(s.containsAll(Arrays.asList(M1.class, M2.class, M3.class)));
    }

    @Test
    public void testMethodWithStereotypesLoop() throws Exception {
        DefaultStereotypeResolution resolution = new DefaultStereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(MethodWithStereotypesLoop.class.getDeclaredMethod("m"))
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(2, s.size());
        assertTrue(s.containsAll(Arrays.asList(Loop1.class, Loop2.class)));
    }
}
