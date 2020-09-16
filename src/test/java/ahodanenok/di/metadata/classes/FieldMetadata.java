package ahodanenok.di.metadata.classes;

import ahodanenok.di.value.Default;
import ahodanenok.di.value.Eager;
import ahodanenok.di.value.Primary;

public class FieldMetadata {

    String defaultMetadata;

    @Primary
    String primaryValue;

    @Default
    String defaultValue;

    @QualifierA
    @QualifierB
    @PackedStereotype
    @Eager
    @InterceptStuff
    Object lotOfMetadata;
}
