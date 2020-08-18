package ahodanenok.di.metadata.classes;

import ahodanenok.di.DefaultValue;
import ahodanenok.di.Eager;

public class FieldMetadata {

    String defaultMetadata;

    @DefaultValue
    String defaultValue;

    @QualifierA
    @QualifierB
    @PackedStereotype
    @Eager
    @InterceptStuff
    Object lotOfMetadata;
}
