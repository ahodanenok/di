package ahodanenok.di.optional;

import ahodanenok.di.Optional;

public class Optionals {

    public Integer checkInt = 1;
    public String checkStr = "1";
    public java.util.Optional checkOptional;

    @Optional
    public int optionalPrimitive;

    @Optional
    public Integer optional = 10;

    public java.util.Optional javaOptional;

    public Optionals() { }

    Optionals(@Optional int n) { }

    Optionals(@Optional Integer n) {
        this.checkInt = n;
    }

    Optionals(java.util.Optional opt) {
        this.checkOptional = opt;
    }

    Optionals(String s, @Optional Integer n) {
        this.checkStr = s;
        this.checkInt = n;
    }

    void methodWithOptionalPrimitiveParameter(@Optional int n) { }

    void methodWithOptionalParameter(@Optional Integer n) {
        this.checkInt = n;
    }

    void methodWithOneOptionalAndNonOptionalParameters(String s, @Optional Integer n) {
        this.checkStr = s;
        this.checkInt = n;
    }

    void methodWithJavaOptional(java.util.Optional opt) {
        this.checkOptional = opt;
    }
}
