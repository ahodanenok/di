package ahodanenok.di.interceptor;

import ahodanenok.di.DIContainer;
import ahodanenok.di.interceptor.classes.AroundConstructInterceptorBinding;
import ahodanenok.di.value.InstanceValue;
import ahodanenok.di.value.InstantiatingValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import static org.junit.jupiter.api.Assertions.*;

public class InterceptorInvocationTest {

    private static InvocationContext ctx;

    @BeforeEach
    private void reset() {
        ctx = null;
    }

    @Test
    public void testAroundConstructInvoked() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstanceValue<>(10))
                .addValue(new InstantiatingValue<>(SimpleInterceptor.class))
                .addValue(new InstantiatingValue<>(InterceptedConstruction.class))
                .build();

        assertNotNull(container.instance(InterceptedConstruction.class));
        assertNotNull(ctx);
        System.out.println(ctx.getTarget());
        assertTrue(ctx.getTarget() instanceof InterceptedConstruction);
        assertEquals(InterceptedConstruction.class.getDeclaredConstructor(int.class), ctx.getConstructor());
        assertArrayEquals(new Object[] { 10 }, ctx.getParameters());
    }

    @Test
    public void testAroundConstructParamsAltered() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstanceValue<>(10))
                .addValue(new InstantiatingValue<>(AlteringParamsInterceptor.class))
                .addValue(new InstantiatingValue<>(InterceptedConstruction.class))
                .build();

        assertNotNull(container.instance(InterceptedConstruction.class));
        assertNotNull(ctx);
        assertTrue(ctx.getTarget() instanceof InterceptedConstruction);
        assertEquals(InterceptedConstruction.class.getDeclaredConstructor(int.class), ctx.getConstructor());
        assertArrayEquals(new Object[] { 20 }, ctx.getParameters());
    }

    @Interceptor
    @AroundConstructInterceptorBinding
    public static class SimpleInterceptor {
        @AroundConstruct
        private void aroundConstructMethod(InvocationContext ctx) throws Exception {
            InterceptorInvocationTest.ctx = ctx;
            ctx.proceed();
        }
    }

    @Interceptor
    @AroundConstructInterceptorBinding
    public static class AlteringParamsInterceptor {
        @AroundConstruct
        private void aroundConstructMethod(InvocationContext ctx) throws Exception {
            InterceptorInvocationTest.ctx = ctx;
            ctx.setParameters(new Object[] { 20 });
            ctx.proceed();
        }
    }

    @AroundConstructInterceptorBinding
    public static class InterceptedConstruction {
        @Inject
        public InterceptedConstruction(int n) { }
    }
}
