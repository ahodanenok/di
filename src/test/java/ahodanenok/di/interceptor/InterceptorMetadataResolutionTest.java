package ahodanenok.di.interceptor;

import ahodanenok.di.interceptor.classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.interceptor.InvocationContext;

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
    public void testInheritedAroundConstructResolved() throws Exception {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        assertEquals(
                AroundConstructInterceptor.class.getDeclaredMethod("aroundConstructMethod", InvocationContext.class),
                resolution.resolveAroundConstruct(AroundConstructInherited.class));
    }

    @Test
    public void testAroundConstructNotResolvedForNotInterceptor() {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        assertNull(resolution.resolveAroundConstruct(AroundConstructNotInterceptor.class));
    }

    @Test
    public void testErrorIfMultipleAroundConstructInAClass() {
        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        assertThrows(Exception.class, () -> resolution.resolveAroundConstruct(AroundConstructMultiple.class));
    }

    @Test
    public void testErrorIfAbstract() {

    }
}
