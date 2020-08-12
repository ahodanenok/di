package ahodanenok.di.interceptor.classes;

import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.function.Consumer;

@Interceptor
public class AroundConstructInterceptor {

    @AroundConstruct
    public void aroundConstructMethod(InvocationContext context) throws Exception {
        context.proceed();
    }
}