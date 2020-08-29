package ahodanenok.di.name;

import ahodanenok.di.DIContainer;
import ahodanenok.di.name.classes.*;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;
import org.junit.jupiter.api.Test;

import javax.inject.Named;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class NamesTest {

    @Test
    public void testClassNotNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertNull(resolution.resolve(NotNamedClass.class));
    }

    @Test
    public void testClassDefaultNamedShortName() {
        @Named class N { }
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("n", resolution.resolve(N.class));
    }

    @Test
    public void testClassDefaultNamedCapitalized() {
        @Named class NNN { }
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("NNN", resolution.resolve(NNN.class));
    }

    @Test
    public void testClassDefaultNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("defaultNamedClass", resolution.resolve(DefaultNamedClass.class));
    }

    @Test
    public void testClassManagedBeanDefaultNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("classManagedBeanDefaultName", resolution.resolve(ClassManagedBeanDefaultName.class));
    }

    @Test
    public void testClassManagedBeanNamesMismatch() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertThrows(IllegalStateException.class, () -> resolution.resolve(ClassNamesMismatch.class));
    }

    @Test
    public void testClassNamedStereotypeWithName() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("classWithDefaultNamedStereotype", resolution.resolve(ClassWithDefaultNamedStereotype.class));
    }

    @Test
    public void testNamedClassWithDefaultNamedStereotype() {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("className", nameResolution.resolve(NamedClassWithDefaultNamedStereotype.class));
    }

    @Test
    public void testClassDefaultNamedViaStereotype() {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertThrows(IllegalStateException.class, () -> nameResolution.resolve(ClassWithNamedStereotype.class));
    }

    @Test
    public void testClassNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("someName", resolution.resolve(NamedClass.class));
    }

    @Test
    public void testFieldNotNamed() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertNull(resolution.resolve(FieldNames.class.getDeclaredField("notNamedField")));
    }

    @Test
    public void testDefaultNamedFieldShort() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("f", resolution.resolve(FieldNames.class.getDeclaredField("f")));
    }

    @Test
    public void testDefaultNamedFieldCapitalized() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("FOO", resolution.resolve(FieldNames.class.getDeclaredField("FOO")));
    }

    @Test
    public void testNamedField() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("fieldName", resolution.resolve(FieldNames.class.getDeclaredField("fieldWithName")));
    }

    @Test
    public void testFieldNamedStereotypeWithName() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution(DIContainer.builder().build());
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        Field f = FieldNames.class.getDeclaredField("fieldWithDefaultNamedStereotype");
        assertEquals("fieldWithDefaultNamedStereotype", nameResolution.resolve(f));
    }

    @Test
    public void testNamedFieldWithDefaultNamedStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution(DIContainer.builder().build());
        Field f = FieldNames.class.getDeclaredField("fieldWithNameAndDefaultNamedStereotype");
        assertEquals("fieldName", nameResolution.resolve(f));
    }

    @Test
    public void testFieldDefaultNamedViaStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution(DIContainer.builder().build());
        Field f = FieldNames.class.getDeclaredField("fieldWithNamedStereotype");
        assertThrows(IllegalStateException.class, () -> nameResolution.resolve(f));
    }

    @Test
    public void testMethodNotNamed() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertNull(resolution.resolve(MethodNames.class.getDeclaredMethod("notNamedMethod")));
    }

    @Test
    public void testDefaultNamedMethodShort() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("m", resolution.resolve(MethodNames.class.getDeclaredMethod("m")));
    }

    @Test
    public void testDefaultNamedMethodProperty() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("test", resolution.resolve(MethodNames.class.getDeclaredMethod("getTest")));
    }

    @Test
    public void testDefaultNamedMethodLikeGetter() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("gettest", resolution.resolve(MethodNames.class.getDeclaredMethod("gettest")));
    }

    @Test
    public void testDefaultNamedGetMethod() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("get", resolution.resolve(MethodNames.class.getDeclaredMethod("get")));
    }

    @Test
    public void testDefaultNamedMethodBooleanProperty() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("m", resolution.resolve(MethodNames.class.getDeclaredMethod("isM")));
    }

    @Test
    public void testDefaultNamedMethodLikeBooleanProperty() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("ism", resolution.resolve(MethodNames.class.getDeclaredMethod("ism")));
    }

    @Test
    public void testNamedMethod() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution(DIContainer.builder().build());
        assertEquals("method", resolution.resolve(MethodNames.class.getDeclaredMethod("namedMethod")));
    }

    @Test
    public void testMethodWithDefaultNamedStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution(DIContainer.builder().build());
        Method m = MethodNames.class.getDeclaredMethod("methodWithDefaultNamedStereotype");
        assertEquals("methodWithDefaultNamedStereotype", nameResolution.resolve(m));
    }

    @Test
    public void testNamedMethodWithDefaultNamedStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution(DIContainer.builder().build());
        Method m = MethodNames.class.getDeclaredMethod("namedMethodWithNamedStereotype");
        assertEquals("methodName", nameResolution.resolve(m));
    }

    @Test
    public void testMethodDefaultNamedViaStereotype() throws Exception {
        AnnotatedNameResolution nameResolution = new AnnotatedNameResolution(DIContainer.builder().build());
        Method m = MethodNames.class.getDeclaredMethod("methodWithNamedStereotype");
        assertThrows(IllegalStateException.class, () -> nameResolution.resolve(m));
    }
}
