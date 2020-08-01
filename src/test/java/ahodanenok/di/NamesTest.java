package ahodanenok.di;

import ahodanenok.di.name.AnnotatedNameResolution;
import org.junit.jupiter.api.Test;

import javax.inject.Named;

import static org.junit.jupiter.api.Assertions.*;

public class NamesTest {

    public static class NotNamedClass { }

    @Named
    public static class DefaultNamedClass { }

    @Named("someName")
    public static class NamedClass { }

    @Test
    public void testClassNotNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertNull(resolution.resolve(NotNamedClass.class));
    }

    @Test
    public void testClassDefaultNamedShortName() {
        @Named class N { }
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("n", resolution.resolve(N.class));
    }

    @Test
    public void testClassDefaultNamedCapitalized() {
        @Named class NNN { }
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("NNN", resolution.resolve(NNN.class));
    }

    @Test
    public void testClassDefaultNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("defaultNamedClass", resolution.resolve(DefaultNamedClass.class));
    }

    @Test
    public void testClassNamed() {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("someName", resolution.resolve(NamedClass.class));
    }

    public static class NotNamedField {
        String f;
    }

    public static class DefaultNamedField {
        @Named String f;
        @Named String FOO;
    }

    public static class NamedField {

        @Named("fieldName") String f;
    }

    @Test
    public void testFieldNotNamed() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertNull(resolution.resolve(NotNamedField.class.getDeclaredField("f")));
    }

    @Test
    public void testDefaultNamedFieldShort() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("f", resolution.resolve(DefaultNamedField.class.getDeclaredField("f")));
    }

    @Test
    public void testDefaultNamedFieldCapitalized() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("FOO", resolution.resolve(DefaultNamedField.class.getDeclaredField("FOO")));
    }

    @Test
    public void testNamedField() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("fieldName", resolution.resolve(NamedField.class.getDeclaredField("f")));
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
    }

    public static class NamedMethod {
        @Named("method") String m() {
            return "m";
        }
    }

    @Test
    public void testMethodNotNamed() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertNull(resolution.resolve(NotNamedMethod.class.getDeclaredMethod("m")));
    }

    @Test
    public void testDefaultNamedMethodShort() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("m", resolution.resolve(DefaultNamedMethod.class.getDeclaredMethod("m")));
    }

    @Test
    public void testDefaultNamedMethodProperty_1() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("test", resolution.resolve(DefaultNamedMethod.class.getDeclaredMethod("getTest")));
    }

    @Test
    public void testDefaultNamedMethodProperty_2() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("gettest", resolution.resolve(DefaultNamedMethod.class.getDeclaredMethod("gettest")));
    }

    @Test
    public void testDefaultNamedMethodProperty_3() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("get", resolution.resolve(DefaultNamedMethod.class.getDeclaredMethod("get")));
    }

    @Test
    public void testDefaultNamedMethodProperty_4() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("m", resolution.resolve(DefaultNamedMethod.class.getDeclaredMethod("isM")));
    }

    @Test
    public void testDefaultNamedMethodProperty_5() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("ism", resolution.resolve(DefaultNamedMethod.class.getDeclaredMethod("ism")));
    }

    @Test
    public void testNamedMethod() throws Exception {
        AnnotatedNameResolution resolution = new AnnotatedNameResolution();
        assertEquals("method", resolution.resolve(NamedMethod.class.getDeclaredMethod("m")));
    }
}
