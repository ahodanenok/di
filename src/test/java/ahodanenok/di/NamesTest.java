package ahodanenok.di;

import ahodanenok.di.name.AnnotatedNameResolution;
import ahodanenok.di.stereotype.DefaultStereotypeResolution;
import ahodanenok.di.stereotype.Stereotype;
import ahodanenok.di.stereotype.StereotypeResolution;
import org.junit.jupiter.api.Test;

import javax.inject.Named;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class NamesTest {

    @Stereotype
    @Named
    @Retention(RetentionPolicy.RUNTIME)
    @interface NamedStereotypeWithDefaultName { }

    @Stereotype
    @Named("nameFromStereotype")
    @Retention(RetentionPolicy.RUNTIME)
    @interface NamedStereotypeWithName { }

    public static class NotNamedClass { }

    @Named
    public static class DefaultNamedClass { }

    @NamedStereotypeWithDefaultName
    public static class DefaultNamedClassViaStereotype { }

    @NamedStereotypeWithDefaultName
    @Named("className")
    public static class NamedClassWithDefaultNamedStereotype { }

    @NamedStereotypeWithName
    public static class ClassWithNamedStereotypeWithName { }

    @Named("someName")
    public static class NamedClass { }

    @Test
    public void testClassNotNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertNull(resolution.resolve(NotNamedClass.class, Collections::emptySet));
    }

    @Test
    public void testClassDefaultNamedShortName() {
        @Named class N { }
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("n", resolution.resolve(N.class, Collections::emptySet));
    }

    @Test
    public void testClassDefaultNamedCapitalized() {
        @Named class NNN { }
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("NNN", resolution.resolve(NNN.class, Collections::emptySet));
    }

    @Test
    public void testClassDefaultNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("defaultNamedClass", resolution.resolve(DefaultNamedClass.class, Collections::emptySet));
    }

    @Test
    public void testClassNamedStereotypeWithName() {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();
        assertEquals(
                "defaultNamedClassViaStereotype",
                nameResolution.resolve(
                        DefaultNamedClassViaStereotype.class,
                        () -> stereotypeResolution.resolve(DefaultNamedClassViaStereotype.class)));
    }

    @Test
    public void testNamedClassWithDefaultNamedStereotype() {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();
        assertEquals(
                "className",
                nameResolution.resolve(
                        NamedClassWithDefaultNamedStereotype.class,
                        () -> stereotypeResolution.resolve(NamedClassWithDefaultNamedStereotype.class)));
    }

    @Test
    public void testClassDefaultNamedViaStereotype() {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();
        assertThrows(IllegalStateException.class,
                () -> nameResolution.resolve(
                        ClassWithNamedStereotypeWithName.class,
                        () -> stereotypeResolution.resolve(ClassWithNamedStereotypeWithName.class)));
    }

    @Test
    public void testClassNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("someName", resolution.resolve(NamedClass.class, Collections::emptySet));
    }

    public static class NotNamedField {
        String f;
    }

    public static class DefaultNamedField {
        @Named String f;
        @Named String FOO;
        @NamedStereotypeWithDefaultName String fd;
    }

    public static class NamedField {

        @Named("fieldName") String f;

        @NamedStereotypeWithDefaultName @Named("fieldName") String fn;
        @NamedStereotypeWithName String fs;
    }

    @Test
    public void testFieldNotNamed() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertNull(resolution.resolve(
                NotNamedField.class.getDeclaredField("f"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedFieldShort() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("f", resolution.resolve(
                DefaultNamedField.class.getDeclaredField("f"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedFieldCapitalized() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("FOO", resolution.resolve(
                DefaultNamedField.class.getDeclaredField("FOO"), Collections::emptySet));
    }

    @Test
    public void testNamedField() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("fieldName", resolution.resolve(
                NamedField.class.getDeclaredField("f"), Collections::emptySet));
    }

    @Test
    public void testFieldNamedStereotypeWithName() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();

        Field f = DefaultNamedField.class.getDeclaredField("fd");
        assertEquals(
                "fd",
                nameResolution.resolve(f, () -> stereotypeResolution.resolve(f)));
    }

    @Test
    public void testNamedFieldWithDefaultNamedStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();

        Field f = NamedField.class.getDeclaredField("fn");
        assertEquals(
                "fieldName",
                nameResolution.resolve(f, () -> stereotypeResolution.resolve(f)));
    }

    @Test
    public void testFieldDefaultNamedViaStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();

        Field f = NamedField.class.getDeclaredField("fs");
        assertThrows(IllegalStateException.class,
                () -> nameResolution.resolve(f, () -> stereotypeResolution.resolve(f)));
    }

    public static class NotNamedMethod {
        String m() {
            return "m";
        }
    }

    public static class DefaultNamedMethod {
        @Named String m() {
            return "m";
        }

        @Named String getTest() {
            return "t";
        }

        @Named String gettest() {
            return "t";
        }

        @Named String get() {
            return "t";
        }

        @Named boolean isM() {
            return false;
        }

        @Named boolean ism() {
            return false;
        }

        @NamedStereotypeWithDefaultName void mn() { };
    }

    public static class NamedMethod {
        @Named("method") String m() {
            return "m";
        }
        @NamedStereotypeWithDefaultName @Named("methodName") void mn() { }
        @NamedStereotypeWithName void ms() { }
    }

    @Test
    public void testMethodNotNamed() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertNull(resolution.resolve(
                NotNamedMethod.class.getDeclaredMethod("m"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodShort() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("m", resolution.resolve(
                DefaultNamedMethod.class.getDeclaredMethod("m"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodProperty_1() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("test", resolution.resolve(
                DefaultNamedMethod.class.getDeclaredMethod("getTest"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodProperty_2() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("gettest", resolution.resolve(
                DefaultNamedMethod.class.getDeclaredMethod("gettest"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodProperty_3() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("get", resolution.resolve(
                DefaultNamedMethod.class.getDeclaredMethod("get"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodProperty_4() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("m", resolution.resolve(
                DefaultNamedMethod.class.getDeclaredMethod("isM"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodProperty_5() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("ism", resolution.resolve(
                DefaultNamedMethod.class.getDeclaredMethod("ism"), Collections::emptySet));
    }

    @Test
    public void testNamedMethod() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("method", resolution.resolve(
                NamedMethod.class.getDeclaredMethod("m"), Collections::emptySet));
    }

    @Test
    public void testMethodNamedStereotypeWithName() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();

        Method m = DefaultNamedMethod.class.getDeclaredMethod("mn");
        assertEquals(
                "mn",
                nameResolution.resolve(m, () -> stereotypeResolution.resolve(m)));
    }

    @Test
    public void testNamedMethodWithDefaultNamedStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();

        Method m = NamedMethod.class.getDeclaredMethod("mn");
        assertEquals(
                "methodName",
                nameResolution.resolve(m, () -> stereotypeResolution.resolve(m)));
    }

    @Test
    public void testMethodDefaultNamedViaStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new DefaultStereotypeResolution();

        Method m = NamedMethod.class.getDeclaredMethod("ms");
        assertThrows(IllegalStateException.class,
                () -> nameResolution.resolve(m, () -> stereotypeResolution.resolve(m)));
    }
}
