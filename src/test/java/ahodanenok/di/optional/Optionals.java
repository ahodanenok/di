package ahodanenok.di.optional;

import java.util.Optional;

public class Optionals {

    public Integer checkInt = 1;
    public String checkStr = "1";
    public Optional<Integer> checkOptional;

    @ahodanenok.di.Optional
    public int optionalPrimitive;

    @ahodanenok.di.Optional
    public Integer optional = 10;

    public Optional<Integer> javaOptional;

    public Optionals() { }

    Optionals(@ahodanenok.di.Optional int n) { }

    Optionals(@ahodanenok.di.Optional Integer n) {
        this.checkInt = n;
    }

    Optionals(Optional<Integer> opt) {
        this.checkOptional = opt;
    }

    Optionals(String s, @ahodanenok.di.Optional Integer n) {
        this.checkStr = s;
        this.checkInt = n;
    }

    void methodWithOptionalPrimitiveParameter(@ahodanenok.di.Optional int n) { }

    void methodWithOptionalParameter(@ahodanenok.di.Optional Integer n) {
        this.checkInt = n;
    }

    void methodWithOneOptionalAndNonOptionalParameters(String s, @ahodanenok.di.Optional Integer n) {
        this.checkStr = s;
        this.checkInt = n;
    }

    void methodWithJavaOptional(Optional<Integer> opt) {
        this.checkOptional = opt;
    }
}
