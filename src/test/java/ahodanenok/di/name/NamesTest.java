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
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertNull(resolution.resolve(NotNamedClass.class));
    }

    @Test
    public void testClassDefaultNamedShortName() {
        @Named class N { }
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("n", resolution.resolve(N.class));
    }

    @Test
    public void testClassDefaultNamedCapitalized() {
        @Named class NNN { }
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("NNN", resolution.resolve(NNN.class));
    }

    @Test
    public void testClassDefaultNamed() {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("defaultNamedClass", resolution.resolve(DefaultNamedClass.class));
    }

    @Test
    public void testClassManagedBeanDefaultNamed() {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("classManagedBeanDefaultName", resolution.resolve(ClassManagedBeanDefaultName.class));
    }

    @Test
    public void testClassManagedBeanNamesMismatch() {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertThrows(IllegalStateException.class, () -> resolution.resolve(ClassNamesMismatch.class));
    }

    @Test
    public void testClassNamedStereotypeWithName() {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("classWithDefaultNamedStereotype", resolution.resolve(ClassWithDefaultNamedStereotype.class));
    }

    @Test
    public void testNamedClassWithDefaultNamedStereotype() {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("className", resolution.resolve(NamedClassWithDefaultNamedStereotype.class));
    }

    @Test
    public void testClassDefaultNamedViaStereotype() {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertThrows(IllegalStateException.class, () -> resolution.resolve(ClassWithNamedStereotype.class));
    }

    @Test
    public void testClassNamed() {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("someName", resolution.resolve(NamedClass.class));
    }

    @Test
    public void testFieldNotNamed() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertNull(resolution.resolve(FieldNames.class.getDeclaredField("notNamedField")));
    }

    @Test
    public void testDefaultNamedFieldShort() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("f", resolution.resolve(FieldNames.class.getDeclaredField("f")));
    }

    @Test
    public void testDefaultNamedFieldCapitalized() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("FOO", resolution.resolve(FieldNames.class.getDeclaredField("FOO")));
    }

    @Test
    public void testNamedField() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("fieldName", resolution.resolve(FieldNames.class.getDeclaredField("fieldWithName")));
    }

    @Test
    public void testFieldNamedStereotypeWithName() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        StereotypeResolution stereotypeResolution = new AnnotatedStereotypeResolution();

        Field f = FieldNames.class.getDeclaredField("fieldWithDefaultNamedStereotype");
        assertEquals("fieldWithDefaultNamedStereotype", resolution.resolve(f));
    }

    @Test
    public void testNamedFieldWithDefaultNamedStereotype() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        Field f = FieldNames.class.getDeclaredField("fieldWithNameAndDefaultNamedStereotype");
        assertEquals("fieldName", resolution.resolve(f));
    }

    @Test
    public void testFieldDefaultNamedViaStereotype() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        Field f = FieldNames.class.getDeclaredField("fieldWithNamedStereotype");
        assertThrows(IllegalStateException.class, () -> resolution.resolve(f));
    }

    @Test
    public void testMethodNotNamed() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertNull(resolution.resolve(MethodNames.class.getDeclaredMethod("notNamedMethod")));
    }

    @Test
    public void testDefaultNamedMethodShort() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("m", resolution.resolve(MethodNames.class.getDeclaredMethod("m")));
    }

    @Test
    public void testDefaultNamedMethodProperty() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("test", resolution.resolve(MethodNames.class.getDeclaredMethod("getTest")));
    }

    @Test
    public void testDefaultNamedMethodLikeGetter() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("gettest", resolution.resolve(MethodNames.class.getDeclaredMethod("gettest")));
    }

    @Test
    public void testDefaultNamedGetMethod() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("get", resolution.resolve(MethodNames.class.getDeclaredMethod("get")));
    }

    @Test
    public void testDefaultNamedMethodBooleanProperty() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("m", resolution.resolve(MethodNames.class.getDeclaredMethod("isM")));
    }

    @Test
    public void testDefaultNamedMethodLikeBooleanProperty() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("ism", resolution.resolve(MethodNames.class.getDeclaredMethod("ism")));
    }

    @Test
    public void testNamedMethod() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        assertEquals("method", resolution.resolve(MethodNames.class.getDeclaredMethod("namedMethod")));
    }

    @Test
    public void testMethodWithDefaultNamedStereotype() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        Method m = MethodNames.class.getDeclaredMethod("methodWithDefaultNamedStereotype");
        assertEquals("methodWithDefaultNamedStereotype", resolution.resolve(m));
    }

    @Test
    public void testNamedMethodWithDefaultNamedStereotype() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        Method m = MethodNames.class.getDeclaredMethod("namedMethodWithNamedStereotype");
        assertEquals("methodName", resolution.resolve(m));
    }

    @Test
    public void testMethodDefaultNamedViaStereotype() throws Exception {
        DIContainer container = DIContainer.builder().build();
        NameResolution resolution = container.instance(NameResolution.class);
        Method m = MethodNames.class.getDeclaredMethod("methodWithNamedStereotype");
        assertThrows(IllegalStateException.class, () -> resolution.resolve(m));
    }
}
