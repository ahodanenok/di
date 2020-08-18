package ahodanenok.di.qualifier.classes;

public class ConstructorMultipleQualifiers {

    @QualifierA
    @QualifierB
    ConstructorMultipleQualifiers() { }

    ConstructorMultipleQualifiers(@QualifierA @QualifierB int n) { }
}
