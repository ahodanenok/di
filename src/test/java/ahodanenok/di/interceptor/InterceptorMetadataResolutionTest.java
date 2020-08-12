package ahodanenok.di.interceptor;

import ahodanenok.di.interceptor.classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.interceptor.InvocationContext;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class InterceptorMetadataResolutionTest {

//    @Test
//    public void testContainerExposesInterceptorMetadataResolution() {
//
//    }

    @Test
    public void testIsInterceptorResolved() {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        assertTrue(resolution.isInterceptor(EmptyInterceptorClass.class));
        assertFalse(resolution.isInterceptor(ExtendingInterceptor.class));
    }

    @Test
    public void testAroundConstructResolved() throws Exception {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        assertEquals(
                AroundConstructInterceptor.class.getDeclaredMethod("aroundConstructMethod", InvocationContext.class),
                resolution.resolveAroundConstruct(AroundConstructInterceptor.class));
    }

    @Test
    public void testInheritedAroundConstructResolvedForInterceptor() throws Exception {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        assertEquals(
                AroundConstructInterceptor.class.getDeclaredMethod("aroundConstructMethod", InvocationContext.class),
                resolution.resolveAroundConstruct(AroundConstructInherited.class));
    }

    @Test
    public void testAroundConstructNotResolvedForNotInterceptor() {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        // todo: error
        assertThrows(Exception.class, () -> resolution.resolveAroundConstruct(AroundConstructNotInterceptor.class));
    }

    @Test
    public void testErrorIfMultipleAroundConstructInAClass() {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        // todo: error
        assertThrows(Exception.class, () -> resolution.resolveAroundConstruct(AroundConstructMultiple.class));
    }

    @Test
    public void testClassBindingsResolved() throws Exception {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        Set<Annotation> bindings = resolution.resolveBindings(
                ClassBindings.class,
                () -> Collections.singleton(ClassBindings.class.getAnnotation(StereotypeWithBinding.class)));

        Set<Class<?>> bindingClasses = bindings.stream().map(Annotation::annotationType).collect(Collectors.toSet());

        assertTrue(bindingClasses.contains(BindingA.class));
        assertTrue(bindingClasses.contains(BindingB.class));
        assertTrue(bindingClasses.contains(BindingC.class));
        assertTrue(bindingClasses.contains(BindingD.class));
        assertTrue(bindingClasses.contains(BindingE.class));
        assertTrue(bindingClasses.contains(BindingWithBinding.class));
        assertTrue(bindingClasses.contains(StereotypeBindingA.class));
        assertTrue(bindingClasses.contains(StereotypeBindingB.class));
    }

    @Test
    public void testConstructorBindingsResolved() throws Exception {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        Set<Annotation> bindings = resolution.resolveBindings(
                ConstructorBindings.class.getDeclaredConstructor(),
                () -> Collections.singleton(ConstructorBindings.class.getAnnotation(StereotypeWithBinding.class)));

        Set<Class<?>> bindingClasses = bindings.stream().map(Annotation::annotationType).collect(Collectors.toSet());

        assertTrue(bindingClasses.contains(BindingA.class));
        assertTrue(bindingClasses.contains(BindingB.class));
        assertTrue(bindingClasses.contains(BindingC.class));
        assertTrue(bindingClasses.contains(BindingD.class));
        assertTrue(bindingClasses.contains(BindingE.class));
        assertTrue(bindingClasses.contains(BindingWithBinding.class));
        assertTrue(bindingClasses.contains(StereotypeBindingA.class));
        assertTrue(bindingClasses.contains(StereotypeBindingB.class));
    }

    @Test
    public void testMethodBindingsResolved() throws Exception {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        Set<Annotation> bindings = resolution.resolveBindings(
                MethodBindings.class.getDeclaredMethod("m"),
                () -> Collections.singleton(MethodBindings.class.getAnnotation(StereotypeWithBinding.class)));

        Set<Class<?>> bindingClasses = bindings.stream().map(Annotation::annotationType).collect(Collectors.toSet());

        assertTrue(bindingClasses.contains(BindingA.class));
        assertTrue(bindingClasses.contains(BindingB.class));
        assertTrue(bindingClasses.contains(BindingC.class));
        assertTrue(bindingClasses.contains(BindingD.class));
        assertTrue(bindingClasses.contains(BindingE.class));
        assertTrue(bindingClasses.contains(BindingWithBinding.class));
        assertTrue(bindingClasses.contains(StereotypeBindingA.class));
        assertTrue(bindingClasses.contains(StereotypeBindingB.class));
    }

    @Test
    public void testErrorIfFinal() {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        // todo: error
        assertThrows(Exception.class, () -> resolution.resolveAroundConstruct(AroundConstructFinal.class));
    }

    @Test
    public void testErrorIfAbstract() {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        // todo: error
        assertThrows(Exception.class, () -> resolution.resolveAroundConstruct(AroundConstructAbstract.class));
    }

    @Test
    public void testErrorIfStatic() {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        // todo: error
        assertThrows(Exception.class, () -> resolution.resolveAroundConstruct(AroundConstructStatic.class));
    }
}
