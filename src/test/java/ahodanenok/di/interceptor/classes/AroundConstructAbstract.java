package ahodanenok.di.interceptor.classes;

import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
public abstract class AroundConstructAbstract {

    @AroundConstruct
    public abstract void m(InvocationContext ctx);
}
