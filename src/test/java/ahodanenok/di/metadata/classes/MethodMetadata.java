package ahodanenok.di.metadata.classes;

import ahodanenok.di.DefaultValue;
import ahodanenok.di.Eager;
import ahodanenok.di.PrimaryValue;

import javax.annotation.Priority;
import javax.interceptor.Interceptor;

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

    @PrimaryValue
    public String primaryValue() {
        return "pv";
    }

    @DefaultValue
    public String defaultValue() {
        return "dv";
    }

    @InterceptStuff
    public Object intercepted() {
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
