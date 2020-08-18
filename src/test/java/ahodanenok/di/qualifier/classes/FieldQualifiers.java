package ahodanenok.di.qualifier.classes;

import ahodanenok.di.DefaultValue;

public class FieldQualifiers {

    @DefaultValue
    private String noQualifiers;

    @QualifierA
    private String singleQualifier;

    @QualifierA
    @QualifierB
    private String multipleQualifiers;

    @R("1")
    @R("2")
    private String repeatableQualifier;
}
