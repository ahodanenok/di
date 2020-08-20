package ahodanenok.di.reflection;

import ahodanenok.di.ReflectionAssistant;
import ahodanenok.di.reflection.classes.*;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionTest {

    @Test
    public void testLookupSelectedDirectlyPresentAnnotations() {
        List<Annotation> result = ReflectionAssistant.annotations(
                AnnotatedChild.class,
                ReflectionAssistant.AnnotationPresence.DIRECTLY,
                A.class, R.class).collect(Collectors.toList());

        assertEquals(1, result.size());
        assertEquals(AnnotatedChild.class.getAnnotation(A.class), result.get(0));
    }

    @Test
    public void testLookupDirectlyPresentAnnotations() {
        List<Annotation> result = ReflectionAssistant.annotations(
                AnnotatedChild.class,
                ReflectionAssistant.AnnotationPresence.DIRECTLY).collect(Collectors.toList());

        assertEquals(3, result.size());
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(A.class)));
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(C.class)));
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(RC.class)));
    }

    @Test
    public void testLookupSelectedIndirectlyPresentAnnotations() {
        List<Annotation> result = ReflectionAssistant.annotations(
                AnnotatedChild.class,
                ReflectionAssistant.AnnotationPresence.INDIRECTLY,
                A.class, R.class).collect(Collectors.toList());

        assertEquals(3, result.size());
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(A.class)));
        assertTrue(result.containsAll(Arrays.asList(AnnotatedChild.class.getAnnotationsByType(R.class))));
    }

    @Test
    public void testLookupIndirectlyPresentAnnotations() {
        List<Annotation> result = ReflectionAssistant.annotations(
                AnnotatedChild.class,
                ReflectionAssistant.AnnotationPresence.INDIRECTLY).collect(Collectors.toList());

        assertEquals(4, result.size());
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(A.class)));
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(C.class)));
        assertTrue(result.containsAll(Arrays.asList(AnnotatedChild.class.getAnnotationsByType(R.class))));
    }

    @Test
    public void testLookupSelectedPresentAnnotations() {
        List<Annotation> result = ReflectionAssistant.annotations(
                AnnotatedChild.class,
                ReflectionAssistant.AnnotationPresence.PRESENT,
                A.class, B.class, R.class).collect(Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(A.class)));
        assertTrue(result.contains(AnnotatedParent.class.getAnnotation(B.class)));
    }

    @Test
    public void testLookupPresentAnnotations() {
        List<Annotation> result = ReflectionAssistant.annotations(
                AnnotatedChild.class,
                ReflectionAssistant.AnnotationPresence.PRESENT).collect(Collectors.toList());

        assertEquals(4, result.size());
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(A.class)));
        assertTrue(result.contains(AnnotatedParent.class.getAnnotation(B.class)));
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(C.class)));
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(RC.class)));
    }

    @Test
    public void testLookupSelectedAssociatedAnnotations() {
        List<Annotation> result = ReflectionAssistant.annotations(
                AnnotatedChild.class,
                ReflectionAssistant.AnnotationPresence.ASSOCIATED,
                A.class, B.class, R.class).collect(Collectors.toList());

        assertEquals(4, result.size());
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(A.class)));
        assertTrue(result.contains(AnnotatedParent.class.getAnnotation(B.class)));
        assertTrue(result.containsAll(Arrays.asList(AnnotatedChild.class.getAnnotationsByType(R.class))));
    }

    @Test
    public void testLookupAssociatedAnnotations() {
        List<Annotation> result = ReflectionAssistant.annotations(
                AnnotatedChild.class,
                ReflectionAssistant.AnnotationPresence.ASSOCIATED).collect(Collectors.toList());

        assertEquals(5, result.size());
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(A.class)));
        assertTrue(result.contains(AnnotatedParent.class.getAnnotation(B.class)));
        assertTrue(result.contains(AnnotatedChild.class.getAnnotation(C.class)));
        assertTrue(result.containsAll(Arrays.asList(AnnotatedChild.class.getAnnotationsByType(R.class))));
    }

    @Test
    public void testRepeatableAnnotationNotInheritingSingleRepeatable() {
        List<Annotation> result = ReflectionAssistant.annotations(
                ClassNotInheritingSingleRepeatable.class,
                ReflectionAssistant.AnnotationPresence.ASSOCIATED).collect(Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(ClassNotInheritingSingleRepeatable.class.getAnnotationsByType(R.class))));
    }
}
