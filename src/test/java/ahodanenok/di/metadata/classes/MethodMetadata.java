package ahodanenok.di.metadata.classes;

import ahodanenok.di.value.Default;
import ahodanenok.di.value.Eager;
import ahodanenok.di.value.Primary;

public class MethodMetadata {

    public String defaultMetadata() {
        return "default";
    }

    @Eager
    public String eager() {
        return "eager";
    }

    @Eager(phase = 10)
    public String eagerPhased() {
        return "eagerPhased";
    }

    @Primary
    public String primaryValue() {
        return "pv";
    }

    @Default
    public String defaultValue() {
        return "dv";
    }

    @InterceptStuff
    public String intercepted() {
        return null;
    }

    @QualifierA
    @QualifierB
    @PackedStereotype
    @Eager
    @InterceptStuff
    public String lotOfMetadata() {
        return "meta";
    }
}
