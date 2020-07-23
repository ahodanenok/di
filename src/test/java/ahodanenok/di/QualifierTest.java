package ahodanenok.di;

import org.junit.jupiter.api.Test;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

public class QualifierTest {

    @ClassQualifier
    static class TestClass {

        @ConstructorQualifier
        TestClass(@ParamQualifier int a) { }

        @FieldQualifier
        long field;

        @MethodQualifier
        void method(@ParamQualifier String b) { }
    }

    @ClassQualifier
    @ClassQualifier2
    static class TestClass_Multiple {

        @ConstructorQualifier
        @ConstructorQualifier2
        TestClass_Multiple(@ParamQualifier @ParamQualifier2 int a) { }

        @FieldQualifier
        @FieldQualifier2
        long field;

        @MethodQualifier
        @MethodQualifier2
        void method(@ParamQualifier @ParamQualifier2 String b) { }
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassQualifier2 { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface FieldQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface FieldQualifier2 { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodQualifier2 { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ConstructorQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ConstructorQualifier2 { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ParamQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ParamQualifier2 { }


    @Test
    public void testQualifierResolution_1() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve((Class<?>) null));
    }

    @Test
    public void testQualifierResolution_2() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve((Method) null));
    }

    @Test
    public void testQualifierResolution_3() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve((Field) null));
    }

    @Test
    public void testQualifierResolution_4() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve((Constructor) null));
    }

    @Test
    public void testQualifierResolution_5() {
        class NoQualifier { }

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertTrue(resolution.resolve(NoQualifier.class).isEmpty());
    }

    @Test
    public void testQualifierResolution_6() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(1, resolution.resolve(TestClass.class).size());
        assertEquals(TestClass.class.getAnnotation(ClassQualifier.class), resolution.resolve(TestClass.class).iterator().next());
    }

    @Test
    public void testQualifierResolution_7() throws Exception {
        Field field = TestClass.class.getDeclaredField("field");

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(1, resolution.resolve(field).size());
        assertEquals(field.getAnnotation(FieldQualifier.class), resolution.resolve(field).iterator().next());
    }


    @Test
    public void testQualifierResolution_8() throws Exception {
        Method method = TestClass.class.getDeclaredMethod("method", String.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(1, resolution.resolve(method).size());
        assertEquals(method.getAnnotation(MethodQualifier.class), resolution.resolve(method).iterator().next());
    }

    @Test
    public void testQualifierResolution_9() throws Exception {
        Method method = TestClass.class.getDeclaredMethod("method", String.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(1, resolution.resolve(method, 0).size());
        assertEquals(method.getParameterAnnotations()[0][0], resolution.resolve(method, 0).iterator().next());
    }

    @Test
    public void testQualifierResolution_10() throws Exception {
        Method method = TestClass.class.getDeclaredMethod("method", String.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve(method, 1));
    }

    @Test
    public void testQualifierResolution_11() throws Exception {
        Method method = TestClass.class.getDeclaredMethod("method", String.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve(method, -1));
    }

    @Test
    public void testQualifierResolution_12() throws Exception {
        Constructor<TestClass> constructor = TestClass.class.getDeclaredConstructor(int.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(1, resolution.resolve(constructor).size());
        assertEquals(constructor.getAnnotation(ConstructorQualifier.class), resolution.resolve(constructor).iterator().next());
    }

    @Test
    public void testQualifierResolution_13() throws Exception {
        Constructor<?> constructor = TestClass.class.getDeclaredConstructor(int.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(1, resolution.resolve(constructor, 0).size());
        assertEquals(constructor.getParameterAnnotations()[0][0], resolution.resolve(constructor, 0).iterator().next());
    }

    @Test
    public void testQualifierResolution_14() throws Exception {
        Constructor<?> constructor = TestClass.class.getDeclaredConstructor(int.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve(constructor, 1));
    }

    @Test
    public void testQualifierResolution_15() throws Exception {
        Constructor<?> constructor = TestClass.class.getDeclaredConstructor(int.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve(constructor, -1));
    }

    @Test
    public void testQualifierResolution_16() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(2, resolution.resolve(TestClass_Multiple.class).size());
        assertTrue(resolution.resolve(TestClass_Multiple.class).containsAll(Arrays.asList(
                TestClass_Multiple.class.getAnnotation(ClassQualifier.class),
                TestClass_Multiple.class.getAnnotation(ClassQualifier2.class))));
    }

    @Test
    public void testQualifierResolution_17() throws Exception {
        Field field = TestClass_Multiple.class.getDeclaredField("field");

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(2, resolution.resolve(field).size());
        assertTrue(resolution.resolve(field).containsAll(Arrays.asList(
                field.getAnnotation(FieldQualifier.class),
                field.getAnnotation(FieldQualifier2.class))));
    }


    @Test
    public void testQualifierResolution_18() throws Exception {
        Method method = TestClass_Multiple.class.getDeclaredMethod("method", String.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(2, resolution.resolve(method).size());
        assertTrue(resolution.resolve(method).containsAll(Arrays.asList(
                method.getAnnotation(MethodQualifier.class),
                method.getAnnotation(MethodQualifier2.class))));
    }

    @Test
    public void testQualifierResolution_19() throws Exception {
        Method method = TestClass_Multiple.class.getDeclaredMethod("method", String.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(2, resolution.resolve(method, 0).size());
        assertTrue(resolution.resolve(method, 0).containsAll(Arrays.asList(
                method.getParameterAnnotations()[0][0],
                method.getParameterAnnotations()[0][1])));
    }

    @Test
    public void testQualifierResolution_20() throws Exception {
        Constructor<?> constructor = TestClass_Multiple.class.getDeclaredConstructor(int.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(2, resolution.resolve(constructor).size());
        assertTrue(resolution.resolve(constructor).containsAll(Arrays.asList(
                constructor.getAnnotation(ConstructorQualifier.class),
                constructor.getAnnotation(ConstructorQualifier2.class))));
    }

    @Test
    public void testQualifierResolution_21() throws Exception {
        Constructor<?> constructor = TestClass_Multiple.class.getDeclaredConstructor(int.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(2, resolution.resolve(constructor, 0).size());
        assertTrue(resolution.resolve(constructor, 0).containsAll(Arrays.asList(
                constructor.getParameterAnnotations()[0][0],
                constructor.getParameterAnnotations()[0][1])));
    }
}
