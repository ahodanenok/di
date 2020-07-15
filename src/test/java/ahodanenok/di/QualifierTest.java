package ahodanenok.di;

import org.junit.jupiter.api.Test;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface FieldQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ConstructorQualifier { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ParamQualifier { }


    @Test
    public void testQualifierResolution_1() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        resolution.resolve((Class<?>) null);
    }

    @Test
    public void testQualifierResolution_2() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        resolution.resolve((Method) null);
    }

    @Test
    public void testQualifierResolution_3() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        resolution.resolve((Field) null);
    }

    @Test
    public void testQualifierResolution_4() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        resolution.resolve((Constructor) null);
    }

    @Test
    public void testQualifierResolution_5() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertNull(resolution.resolve(TestClass.class));
    }

    @Test
    public void testQualifierResolution_6() {
        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(TestClass.class.getAnnotation(ClassQualifier.class), resolution.resolve(TestClass.class));
    }

    @Test
    public void testQualifierResolution_7() throws Exception {
        Field field = TestClass.class.getDeclaredField("field");

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(field.getAnnotation(FieldQualifier.class), resolution.resolve(field));
    }


    @Test
    public void testQualifierResolution_8() throws Exception {
        Method method = TestClass.class.getDeclaredMethod("method", String.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(method.getAnnotation(MethodQualifier.class), resolution.resolve(method));
    }

    @Test
    public void testQualifierResolution_9() throws Exception {
        Method method = TestClass.class.getDeclaredMethod("method", String.class);

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(method.getParameterAnnotations()[0][0], resolution.resolve(method, 0));
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
        Constructor<TestClass> constructor = TestClass.class.getConstructor();

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(constructor.getAnnotation(ConstructorQualifier.class), resolution.resolve(constructor));
    }

    @Test
    public void testQualifierResolution_13() throws Exception {
        Constructor<?> constructor = TestClass.class.getConstructor();

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertEquals(constructor.getParameterAnnotations()[0][0], resolution.resolve(constructor, 0));
    }

    @Test
    public void testQualifierResolution_14() throws Exception {
        Constructor<?> constructor = TestClass.class.getConstructor();

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve(constructor, 1));
    }

    @Test
    public void testQualifierResolution_15() throws Exception {
        Constructor<?> constructor = TestClass.class.getConstructor();

        AnnotatedQualifierResolution resolution = new AnnotatedQualifierResolution();
        assertThrows(IllegalArgumentException.class, () -> resolution.resolve(constructor, -1));
    }
}
