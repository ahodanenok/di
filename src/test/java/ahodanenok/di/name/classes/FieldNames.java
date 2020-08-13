package ahodanenok.di.name.classes;

import javax.inject.Named;

public class FieldNames {

    String notNamedField;

    @Named
    String f;

    @Named
    String FOO;

    @DefaultNamedStereotype
    String fieldWithDefaultNamedStereotype;

    @Named("fieldName")
    String fieldWithName;

    @DefaultNamedStereotype
    @Named("fieldName")
    String fieldWithNameAndDefaultNamedStereotype;

    @NamedStereotype
    String fieldWithNamedStereotype;
}
