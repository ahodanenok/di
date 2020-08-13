package ahodanenok.di.name.classes;

import javax.inject.Named;

public class MethodNames {

    String notNamedMethod() {
        return "m";
    }

    @Named
    String m() {
        return "m";
    }

    @Named
    String getTest() {
        return "t";
    }

    @Named
    String gettest() {
        return "t";
    }

    @Named
    String get() {
        return "t";
    }

    @Named
    boolean isM() {
        return false;
    }

    @Named
    boolean ism() {
        return false;
    }

    @DefaultNamedStereotype
    void methodWithDefaultNamedStereotype() { };

    @Named("method")
    String namedMethod() {
        return "m";
    }

    @DefaultNamedStereotype
    @Named("methodName")
    void namedMethodWithNamedStereotype() { }

    @NamedStereotype
    void methodWithNamedStereotype() { }
}
