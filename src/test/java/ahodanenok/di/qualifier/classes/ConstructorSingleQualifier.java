package ahodanenok.di.qualifier.classes;

public class ConstructorSingleQualifier {

    @QualifierA
    ConstructorSingleQualifier() { }

    ConstructorSingleQualifier(@QualifierA int n) { }
}
