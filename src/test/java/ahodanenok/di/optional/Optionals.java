package ahodanenok.di.optional;

import ahodanenok.di.OptionalDependency;

import javax.inject.Inject;
import java.util.Optional;

public class Optionals {

    public Integer checkInt = 1;
    public String checkStr = "1";
    public Optional<Integer> checkOptional;

    @OptionalDependency
    public int optionalPrimitive;

    @OptionalDependency
    public Integer optional = 10;

    public Optional<Integer> javaOptional;

    public Optionals() { }

    Optionals(@OptionalDependency int n) { }

    Optionals(@OptionalDependency Integer n) {
        this.checkInt = n;
    }

    Optionals(Optional<Integer> opt) {
        this.checkOptional = opt;
    }

    Optionals(String s, @OptionalDependency Integer n) {
        this.checkStr = s;
        this.checkInt = n;
    }

    void methodWithOptionalPrimitiveParameter(@OptionalDependency int n) { }

    void methodWithOptionalParameter(@OptionalDependency Integer n) {
        this.checkInt = n;
    }

    void methodWithOneOptionalAndNonOptionalParameters(String s, @OptionalDependency Integer n) {
        this.checkStr = s;
        this.checkInt = n;
    }

    void methodWithJavaOptional(Optional<Integer> opt) {
        this.checkOptional = opt;
    }
}
