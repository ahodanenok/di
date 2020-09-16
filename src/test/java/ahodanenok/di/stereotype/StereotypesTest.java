package ahodanenok.di.stereotype;

import ahodanenok.di.stereotype.classes.*;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class StereotypesTest {

    @Test
    public void testClassNoStereotypes() {
        StereotypeResolution resolution = new StereotypeResolution();
        assertTrue(resolution.resolve(ClassNoStereotypes.class).isEmpty());
    }

    @Test
    public void testClassSingleStereotype() {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Annotation> s = resolution.resolve(ClassSingleStereotype.class);
        assertEquals(1, s.size());
        assertEquals(StereotypeA.class, s.iterator().next().annotationType());
    }

    @Test
    public void testClassWithInheritedStereotype() {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(ClassWithInheritedStereotype.class)
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());
        assertEquals(3, s.size());
        assertTrue(s.containsAll(Arrays.asList(StereotypeA.class, StereotypeB.class, InheritedStereotype.class)));
    }

    @Test
    public void testClassWithStereotypeWithStereotypes() {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(ClassWithStereotypedStereotype.class)
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(3, s.size());
        assertTrue(s.containsAll(Arrays.asList(StereotypeWithStereotypeA.class, StereotypeWithStereotypeB.class, StereotypeWithStereotypeC.class)));
    }

    @Test
    public void testClassStereotypeLoop() {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(ClassWithStereotypeLoop.class)
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(2, s.size());
        assertTrue(s.containsAll(Arrays.asList(Loop1.class, Loop2.class)));
    }

    @Test
    public void testFieldNoStereotypes() throws Exception {
        StereotypeResolution resolution = new StereotypeResolution();
        assertTrue(resolution.resolve(FieldStereotypes.class.getDeclaredField("noStereotypes")).isEmpty());
    }

    @Test
    public void testFieldSingleStereotype() throws Exception {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Annotation> s = resolution.resolve(FieldStereotypes.class.getDeclaredField("singleStereotype"));
        assertEquals(1, s.size());
        assertEquals(StereotypeA.class, s.iterator().next().annotationType());
    }

    @Test
    public void testFieldWithStereotypeWithStereotypes() throws Exception {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(FieldStereotypes.class.getDeclaredField("stereotypesWithStereotypes"))
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(3, s.size());
        assertTrue(s.containsAll(Arrays.asList(
                StereotypeWithStereotypeA.class, StereotypeWithStereotypeB.class, StereotypeWithStereotypeC.class)));
    }

    @Test
    public void testFieldWithStereotypesLoop() throws Exception {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(FieldStereotypes.class.getDeclaredField("stereotypesLoop"))
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(2, s.size());
        assertTrue(s.containsAll(Arrays.asList(Loop1.class, Loop2.class)));
    }

    @Test
    public void testMethodNoStereotypes() throws Exception {
        StereotypeResolution resolution = new StereotypeResolution();
        assertTrue(resolution.resolve(MethodStereotypes.class.getDeclaredMethod("noStereotypes")).isEmpty());
    }

    @Test
    public void testMethodSingleStereotype() throws Exception {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Annotation> s = resolution.resolve(MethodStereotypes.class.getDeclaredMethod("singleStereotype"));
        assertEquals(1, s.size());
        assertEquals(StereotypeA.class, s.iterator().next().annotationType());
    }

    @Test
    public void testMethodWithStereotypeWithStereotypes() throws Exception {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(MethodStereotypes.class.getDeclaredMethod("stereotypesWithStereotypes"))
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(3, s.size());
        assertTrue(s.containsAll(Arrays.asList(
                StereotypeWithStereotypeA.class, StereotypeWithStereotypeB.class, StereotypeWithStereotypeC.class)));
    }

    @Test
    public void testMethodWithStereotypesLoop() throws Exception {
        StereotypeResolution resolution = new StereotypeResolution();
        Set<Class<? extends Annotation>> s = resolution.resolve(MethodStereotypes.class.getDeclaredMethod("stereotypesLoop"))
                .stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        assertEquals(2, s.size());
        assertTrue(s.containsAll(Arrays.asList(Loop1.class, Loop2.class)));
    }
}
