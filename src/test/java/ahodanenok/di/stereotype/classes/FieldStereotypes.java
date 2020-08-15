package ahodanenok.di.stereotype.classes;

public class FieldStereotypes {

    String noStereotypes;

    @StereotypeA
    String singleStereotype;

    @StereotypeWithStereotypeA
    String stereotypesWithStereotypes;

    @Loop1
    String stereotypesLoop;
}
