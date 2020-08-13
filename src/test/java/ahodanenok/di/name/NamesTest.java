package ahodanenok.di.name;

import ahodanenok.di.name.classes.*;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;
import org.junit.jupiter.api.Test;

import javax.inject.Named;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class NamesTest {

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
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();
        assertEquals(
                "classWithDefaultNamedStereotype",
                nameResolution.resolve(
                        ClassWithDefaultNamedStereotype.class,
                        () -> stereotypeResolution.resolve(ClassWithDefaultNamedStereotype.class)));
    }

    @Test
    public void testNamedClassWithDefaultNamedStereotype() {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();
        assertEquals(
                "className",
                nameResolution.resolve(
                        NamedClassWithDefaultNamedStereotype.class,
                        () -> stereotypeResolution.resolve(NamedClassWithDefaultNamedStereotype.class)));
    }

    @Test
    public void testClassDefaultNamedViaStereotype() {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();
        assertThrows(IllegalStateException.class,
                () -> nameResolution.resolve(
                        ClassWithNamedStereotype.class,
                        () -> stereotypeResolution.resolve(ClassWithNamedStereotype.class)));
    }

    @Test
    public void testClassNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("someName", resolution.resolve(NamedClass.class, Collections::emptySet));
    }

    @Test
    public void testFieldNotNamed() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertNull(resolution.resolve(
                FieldNames.class.getDeclaredField("notNamedField"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedFieldShort() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("f", resolution.resolve(
                FieldNames.class.getDeclaredField("f"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedFieldCapitalized() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("FOO", resolution.resolve(
                FieldNames.class.getDeclaredField("FOO"), Collections::emptySet));
    }

    @Test
    public void testNamedField() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("fieldName", resolution.resolve(
                FieldNames.class.getDeclaredField("fieldWithName"), Collections::emptySet));
    }

    @Test
    public void testFieldNamedStereotypeWithName() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        Field f = FieldNames.class.getDeclaredField("fieldWithDefaultNamedStereotype");
        assertEquals(
                "fieldWithDefaultNamedStereotype",
                nameResolution.resolve(f, () -> stereotypeResolution.resolve(f)));
    }

    @Test
    public void testNamedFieldWithDefaultNamedStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        Field f = FieldNames.class.getDeclaredField("fieldWithNameAndDefaultNamedStereotype");
        assertEquals(
                "fieldName",
                nameResolution.resolve(f, () -> stereotypeResolution.resolve(f)));
    }

    @Test
    public void testFieldDefaultNamedViaStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        Field f = FieldNames.class.getDeclaredField("fieldWithNamedStereotype");
        assertThrows(IllegalStateException.class,
                () -> nameResolution.resolve(f, () -> stereotypeResolution.resolve(f)));
    }

    @Test
    public void testMethodNotNamed() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertNull(resolution.resolve(
                MethodNames.class.getDeclaredMethod("notNamedMethod"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodShort() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("m", resolution.resolve(
                MethodNames.class.getDeclaredMethod("m"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodProperty() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("test", resolution.resolve(
                MethodNames.class.getDeclaredMethod("getTest"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodLikeGetter() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("gettest", resolution.resolve(
                MethodNames.class.getDeclaredMethod("gettest"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedGetMethod() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("get", resolution.resolve(
                MethodNames.class.getDeclaredMethod("get"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodBooleanProperty() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("m", resolution.resolve(
                MethodNames.class.getDeclaredMethod("isM"), Collections::emptySet));
    }

    @Test
    public void testDefaultNamedMethodLikeBooleanProperty() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("ism", resolution.resolve(
                MethodNames.class.getDeclaredMethod("ism"), Collections::emptySet));
    }

    @Test
    public void testNamedMethod() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("method", resolution.resolve(
                MethodNames.class.getDeclaredMethod("namedMethod"), Collections::emptySet));
    }

    @Test
    public void testMethodWithDefaultNamedStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        Method m = MethodNames.class.getDeclaredMethod("methodWithDefaultNamedStereotype");
        assertEquals(
                "methodWithDefaultNamedStereotype",
                nameResolution.resolve(m, () -> stereotypeResolution.resolve(m)));
    }

    @Test
    public void testNamedMethodWithDefaultNamedStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        Method m = MethodNames.class.getDeclaredMethod("namedMethodWithNamedStereotype");
        assertEquals(
                "methodName",
                nameResolution.resolve(m, () -> stereotypeResolution.resolve(m)));
    }

    @Test
    public void testMethodDefaultNamedViaStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution();
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        Method m = MethodNames.class.getDeclaredMethod("methodWithNamedStereotype");
        assertThrows(IllegalStateException.class,
                () -> nameResolution.resolve(m, () -> stereotypeResolution.resolve(m)));
    }
}
