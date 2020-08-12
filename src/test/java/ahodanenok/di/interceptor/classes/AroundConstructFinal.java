package ahodanenok.di.interceptor.classes;

import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
public class AroundConstructFinal {

    @AroundConstruct
    public final void m(InvocationContext ctx) { }
}
