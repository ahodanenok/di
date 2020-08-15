package ahodanenok.di.stereotype.classes;

public class MethodStereotypes {

    void noStereotypes() { }

    @StereotypeA
    void singleStereotype() { }

    @StereotypeWithStereotypeA
    void stereotypesWithStereotypes() { }

    @Loop1
    void stereotypesLoop() { }
}
