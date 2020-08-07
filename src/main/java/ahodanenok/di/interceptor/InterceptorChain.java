package ahodanenok.di.interceptor;

import java.lang.reflect.Method;
import java.util.List;

public class InterceptorChain {

    private AroundConstruct<?> aroundConstruct;

    public InterceptorChain(AroundConstruct<?> aroundConstruct, List<Method> interceptorMethods) {
        this.aroundConstruct = aroundConstruct;
    }

    public void proceed() throws Exception {
        aroundConstruct.proceed();
    }
}
