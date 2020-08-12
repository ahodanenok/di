package ahodanenok.di.interceptor.classes;

import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
public class AroundConstructMultiple {

    @AroundConstruct
    public void a(InvocationContext context) { }

    @AroundConstruct
    public void b(InvocationContext context) { }
}
