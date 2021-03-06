package ahodanenok.di.qualifier;

import ahodanenok.di.ReflectionAssistant;
import ahodanenok.di.qualifier.classes.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class QualifierTest {

    @Test
    public void testClassWithoutQualifiers() {
        QualifierResolution resolution = new QualifierResolution();
        assertTrue(resolution.resolve(NoClassQualifiers.class).isEmpty());
    }

    @Test
    public void testClassWithSingleQualifier() {
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(SingleClassQualifier.class);
        assertEquals(1, qualifiers.size());
        assertEquals(SingleClassQualifier.class.getAnnotation(QualifierA.class), qualifiers.iterator().next());
    }

    @Test
    public void testClassWithMultipleQualifiers() {
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(MultipleClassQualifiers.class);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.contains(MultipleClassQualifiers.class.getAnnotation(QualifierA.class)));
        assertTrue(qualifiers.contains(MultipleClassQualifiers.class.getAnnotation(QualifierB.class)));
    }

    @Test
    public void testClassWithInheritedQualifier() {
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(InheritedClassQualifier.class);
        assertEquals(1, qualifiers.size());
        assertEquals(SingleClassQualifier.class.getAnnotation(QualifierA.class), qualifiers.iterator().next());
    }

    @Test
    public void testClassWithRepeatableQualifiers() {
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(ClassRepeatableQualifiers.class);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.containsAll(Arrays.asList(ClassRepeatableQualifiers.class.getAnnotationsByType(R.class))));
    }

    @Test
    public void testFieldNoQualifiers() throws Exception {
        Field field = FieldQualifiers.class.getDeclaredField("noQualifiers");
        QualifierResolution resolution = new QualifierResolution();
        assertTrue(resolution.resolve(field).isEmpty());
    }

    @Test
    public void testFieldWithSingleQualifier() throws Exception {
        Field field = FieldQualifiers.class.getDeclaredField("singleQualifier");
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(field);
        assertEquals(1, qualifiers.size());
        assertEquals(field.getAnnotation(QualifierA.class), qualifiers.iterator().next());
    }

    @Test
    public void testFieldWithMultipleQualifiers() throws Exception {
        Field field = FieldQualifiers.class.getDeclaredField("multipleQualifiers");
        QualifierResolution resolution = new QualifierResolution();
        assertEquals(2, resolution.resolve(field).size());
        assertTrue(resolution.resolve(field).containsAll(Arrays.asList(
                field.getAnnotation(QualifierA.class),
                field.getAnnotation(QualifierB.class))));
    }

    @Test
    public void testFieldWithRepeatableQualifiers() throws Exception {
        Field field = FieldQualifiers.class.getDeclaredField("repeatableQualifier");
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(field);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.containsAll(Arrays.asList(field.getAnnotationsByType(R.class))));
    }

    @Test
    public void testMethodWithNoQualifiers() throws Exception {
        Method method = MethodQualifiers.class.getDeclaredMethod("noQualifiers");
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(method);
        assertTrue(qualifiers.isEmpty());
    }

    @Test
    public void testMethodWithSingleQualifier() throws Exception {
        Method method = MethodQualifiers.class.getDeclaredMethod("singleQualifier");
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(method);
        assertEquals(1, qualifiers.size());
        assertEquals(method.getAnnotation(QualifierA.class), qualifiers.iterator().next());
    }

    @Test
    public void testMethodWithMultipleQualifiers() throws Exception {
        Method method = MethodQualifiers.class.getDeclaredMethod("multipleQualifiers");
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(method);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.contains(method.getAnnotation(QualifierA.class)));
        assertTrue(qualifiers.contains(method.getAnnotation(QualifierB.class)));
    }

    @Test
    public void testMethodWithRepeatableQualifiers() throws Exception {
        Method method = MethodQualifiers.class.getDeclaredMethod("multipleQualifiers");
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(method);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.containsAll(Arrays.asList(method.getAnnotationsByType(R.class))));
    }

    @Test
    public void testMethodParameterWithNoQualifiers() throws Exception {
        Method method = MethodQualifiers.class.getDeclaredMethod("noParameterQualifiers", String.class);
        QualifierResolution resolution = new QualifierResolution();
        assertTrue(resolution.resolve(method.getParameters()[0]).isEmpty());
    }

    @Test
    public void testMethodParameterWithSingleQualifier() throws Exception {
        Method method = MethodQualifiers.class.getDeclaredMethod("singleParameterQualifier", String.class);
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(method.getParameters()[0]);
        assertEquals(1, qualifiers.size());
        assertEquals(method.getParameterAnnotations()[0][0], qualifiers.iterator().next());
    }
    @Test
    public void testMethodParameterWithMultipleQualifiers() throws Exception {
        Method method = MethodQualifiers.class.getDeclaredMethod("multipleParameterQualifiers", String.class);
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(method.getParameters()[0]);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.containsAll(Arrays.asList(
                method.getParameterAnnotations()[0][0],
                method.getParameterAnnotations()[0][1])));
    }

//    @Test
//    public void testNotExistingMethodParameterQualifiers() throws Exception {
//        Method method = MethodQualifiers.class.getDeclaredMethod("noQualifiers");
//        QualifierResolution resolution = new QualifierResolution();
//        assertThrows(IllegalArgumentException.class, () -> resolution.resolve(method, 0));
//    }

    class A {
        @Inject
        A(@QualifierA int a) { }
    }

    @Test
    public void testConstructorQualifiersForInnerClass() throws Exception {
        Constructor<?> constructor = A.class.getDeclaredConstructor(QualifierTest.class, int.class);
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(constructor.getParameters()[1]);
        assertEquals(1, qualifiers.size());
//        assertEquals(constructor.getParameterAnnotations()[1][0], qualifiers.iterator().next());
        assertEquals(
                ReflectionAssistant.parameterAnnotations(constructor, 1, ReflectionAssistant.AnnotationPresence.DIRECTLY).findFirst().orElse(null),
                qualifiers.iterator().next());
    }

    @Test
    public void testConstructorNoQualifiers() throws Exception {
        Constructor<ConstructorNoQualifiers> constructor = ConstructorNoQualifiers.class.getDeclaredConstructor();
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(constructor);
        assertTrue(qualifiers.isEmpty());
    }

    @Test
    public void testConstructorSingleQualifier() throws Exception {
        Constructor<ConstructorSingleQualifier> constructor = ConstructorSingleQualifier.class.getDeclaredConstructor();
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(constructor);
        assertEquals(1, qualifiers.size());
        assertEquals(constructor.getAnnotation(QualifierA.class), qualifiers.iterator().next());
    }

    @Test
    public void testConstructorMultipleQualifiers() throws Exception {
        Constructor<ConstructorMultipleQualifiers> constructor = ConstructorMultipleQualifiers.class.getDeclaredConstructor();
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(constructor);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.contains(constructor.getAnnotation(QualifierA.class)));
        assertTrue(qualifiers.contains(constructor.getAnnotation(QualifierB.class)));
    }

    @Test
    public void testConstructorRepeatableQualifier() throws Exception {
        Constructor<ConstructorRepeatableQualifier> constructor = ConstructorRepeatableQualifier.class.getDeclaredConstructor();
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(constructor);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.containsAll(Arrays.asList(constructor.getAnnotationsByType(R.class))));
    }

    @Test
    public void testConstructorParameterNoQualifiers() throws Exception {
        Constructor<?> constructor = ConstructorNoQualifiers.class.getDeclaredConstructor(int.class);
        QualifierResolution resolution = new QualifierResolution();
        assertTrue(resolution.resolve(constructor.getParameters()[0]).isEmpty());
    }

    @Test
    public void testConstructorParameterSingleQualifiers() throws Exception {
        Constructor<?> constructor = ConstructorSingleQualifier.class.getDeclaredConstructor(int.class);
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(constructor.getParameters()[0]);
        assertEquals(1, qualifiers.size());
        assertEquals(constructor.getParameterAnnotations()[0][0], qualifiers.iterator().next());
    }

    @Test
    public void testConstructorParameterMultipleQualifiers() throws Exception {
        Constructor<?> constructor = ConstructorMultipleQualifiers.class.getDeclaredConstructor(int.class);
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(constructor.getParameters()[0]);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.contains(constructor.getParameterAnnotations()[0][0]));
        assertTrue(qualifiers.contains(constructor.getParameterAnnotations()[0][1]));
    }

    @Test
    public void testConstructorParameterRepeatableQualifier() throws Exception {
        Constructor<?> constructor = ConstructorRepeatableQualifier.class.getDeclaredConstructor(int.class);
        QualifierResolution resolution = new QualifierResolution();
        Set<Annotation> qualifiers = resolution.resolve(constructor.getParameters()[0]);
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.containsAll(Arrays.asList(constructor.getParameters()[0].getAnnotationsByType(R.class))));
    }

//    @Test
//    public void testNotExistingQualifierParameterQualifiers() throws Exception {
//        Constructor<?> constructor = ConstructorSingleQualifier.class.getDeclaredConstructor(int.class);
//
//        QualifierResolution resolution = new QualifierResolution();
//        assertThrows(IllegalArgumentException.class, () -> resolution.resolve(constructor, 1));
//    }
}
