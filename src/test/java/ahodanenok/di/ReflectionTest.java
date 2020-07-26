package ahodanenok.di;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface A {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface B {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface RC {
        R[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(RC.class)
    @Inherited
    @interface R {
        String value();
    }

    @A("PA1")
    @R("PR1")
    @R("PR2")
    @R("PR3")
    static class Parent { }

    @A("CA1")
    @R("CR1")
    @R("CR2")
    static class ChildA extends Parent { }

    @B("CB1")
    @R("CR1")
    @R("CR2")
    static class ChildB extends Parent { }

    @Test
    public void testAnnotationsDirectly_1() {
        List<Annotation> result = ReflectionAssistant.annotations(
                ChildA.class,
                ReflectionAssistant.AnnotationPresence.DIRECTLY,
                A.class, B.class, R.class).collect(Collectors.toList());

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof A);
        assertEquals("CA1", ((A) result.get(0)).value());
    }

    @Test
    public void testAnnotationsIndirectly_1() {
        List<Annotation> result = ReflectionAssistant.annotations(
                ChildA.class,
                ReflectionAssistant.AnnotationPresence.INDIRECTLY,
                A.class, B.class, R.class).collect(Collectors.toList());

        assertEquals(3, result.size());
        assertTrue(result.get(0) instanceof A);
        assertEquals("CA1", ((A) result.get(0)).value());
        assertTrue(result.get(1) instanceof R);
        assertEquals("CR1", ((R) result.get(1)).value());
        assertTrue(result.get(2) instanceof R);
        assertEquals("CR2", ((R) result.get(2)).value());
    }

    @Test
    public void testAnnotationsPresent_1() {
        List<Annotation> result = ReflectionAssistant.annotations(
                ChildB.class,
                ReflectionAssistant.AnnotationPresence.PRESENT,
                A.class, B.class, R.class).collect(Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof A);
        assertEquals("PA1", ((A) result.get(0)).value());
        assertTrue(result.get(1) instanceof B);
        assertEquals("CB1", ((B) result.get(1)).value());
    }

    @Test
    public void testAnnotationsAssociated_1() {
        List<Annotation> result = ReflectionAssistant.annotations(
                ChildB.class,
                ReflectionAssistant.AnnotationPresence.ASSOCIATED,
                A.class, B.class, R.class).collect(Collectors.toList());

        assertEquals(4, result.size());
        assertTrue(result.get(0) instanceof A);
        assertEquals("PA1", ((A) result.get(0)).value());
        assertTrue(result.get(1) instanceof B);
        assertEquals("CB1", ((B) result.get(1)).value());
        assertTrue(result.get(2) instanceof R);
        assertEquals("CR1", ((R) result.get(2)).value());
        assertTrue(result.get(3) instanceof R);
        assertEquals("CR2", ((R) result.get(3)).value());
    }
}
