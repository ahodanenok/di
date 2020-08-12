package ahodanenok.di.interceptor.classes;

@BindingA
@BindingC
@StereotypeWithBinding
public class ConstructorBindings extends ConstructorBindingsParent {

    @BindingA
    @BindingB
    public ConstructorBindings() { }
}

@BindingWithBinding
class ConstructorBindingsParent { }
