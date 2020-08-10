package ahodanenok.di;

import ahodanenok.di.interceptor.AnnotatedInterceptorMetadataResolution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.interceptor.AroundConstruct;
import javax.interceptor.InvocationContext;

public class InterceptorsTest {

    @Test
    public void testFindingAroundConstructorMethodsInClass() throws Exception {
        class A {
            @AroundConstruct
            public void m(InvocationContext context) { }
        }

        AnnotatedInterceptorMetadataResolution resolution = new AnnotatedInterceptorMetadataResolution();
        Assertions.assertEquals(A.class.getDeclaredMethod("m", InvocationContext.class), resolution.resolveAroundConstruct(A.class));

//        DIContainer container = DIContainer.builder().build();
//        InterceptorRegistry registry = new InterceptorRegistry(container, Collections.singletonList(A.class));
//        registry.aroundConstructInterceptorMethods()
    }
}
