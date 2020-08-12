package ahodanenok.di.interceptor.classes;

import javax.interceptor.AroundConstruct;
import javax.interceptor.InvocationContext;

public class AroundConstructNotInterceptor {

    @AroundConstruct
    public void aroundConstructMethod(InvocationContext context) { }
}
