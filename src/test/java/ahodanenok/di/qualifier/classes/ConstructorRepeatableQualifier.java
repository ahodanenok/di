package ahodanenok.di.qualifier.classes;

public class ConstructorRepeatableQualifier {

    @R("1")
    @R("2")
    ConstructorRepeatableQualifier() { }

    ConstructorRepeatableQualifier(@R("1") @R("2") int n) { }
}
