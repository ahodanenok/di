package ahodanenok.di.interceptor.classes;

@BindingA
@BindingC
@StereotypeWithBinding
public class MethodBindings extends MethodBindingsParent {

    @BindingA
    @BindingB
    public void m() { }
}

@BindingWithBinding
class MethodBindingsParent { }
