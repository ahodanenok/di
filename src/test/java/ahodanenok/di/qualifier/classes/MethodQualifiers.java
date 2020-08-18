package ahodanenok.di.qualifier.classes;

import javax.inject.Inject;

public class MethodQualifiers {

    @Inject
    private String noQualifiers() {
        return null;
    }

    @QualifierA
    private String singleQualifier() {
        return null;
    }

    @QualifierA
    @QualifierB
    private String multipleQualifiers() {
        return null;
    }

    @R("1")
    @R("2")
    private String repeatableQualifier() {
        return null;
    }

    private void noParameterQualifiers(String s) { }
    private void singleParameterQualifier(@QualifierA String s) { }
    private void multipleParameterQualifiers(@QualifierA @QualifierB String s) { }
    private void repeatableParameterQualifier(@R("1") @R("2") String s) { }
}
