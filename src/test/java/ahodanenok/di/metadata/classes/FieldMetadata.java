package ahodanenok.di.metadata.classes;

import ahodanenok.di.DefaultValue;
import ahodanenok.di.Eager;
import ahodanenok.di.PrimaryValue;

public class FieldMetadata {

    String defaultMetadata;

    @PrimaryValue
    String primaryValue;

    @DefaultValue
    String defaultValue;

    @QualifierA
    @QualifierB
    @PackedStereotype
    @Eager
    @InterceptStuff
    Object lotOfMetadata;
}
