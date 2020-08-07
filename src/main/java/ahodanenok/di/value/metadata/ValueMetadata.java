package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.Set;

public interface ValueMetadata<T> {

    Class<? extends T> valueType();

    String name();

    ScopeIdentifier scope();

    boolean isDefault();

    boolean isEager();

    Set<Annotation> qualifiers();

    Set<Annotation> stereotypes();
}
