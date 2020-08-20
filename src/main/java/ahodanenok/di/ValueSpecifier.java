package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.util.*;

// todo: create annotation instance based on definition like in Annotation.toString (@com.acme.util.Name(first=Alfred, middle=E., last=Neuman))
// DependencyIdentifier.of(String.class, "@javax.inject.Named(value=Test)");

// todo: type with wildcard type parameter is not valid
public final class ValueSpecifier<T> {

    public static <T> ValueSpecifier<T> of(Class<T> type) {
        return of(type, Collections.emptySet());
    }

    public static <T> ValueSpecifier<T> of(Class<T> type, Set<Annotation> qualifiers) {
        ValueSpecifier<T> specifier = new ValueSpecifier<>();
        specifier.type = type;
        specifier.qualifiers = qualifiers != null ? new LinkedHashSet<>(qualifiers) : Collections.emptySet();
        return specifier;
    }

    public static <T> ValueSpecifier<T> of(Class<T> type, Annotation... qualifiers) {
        return of(type, new LinkedHashSet<>(Arrays.asList(qualifiers)));
    }

    public static ValueSpecifier<?> named(String name) {
        ValueSpecifier<?> specifier = new ValueSpecifier<>();
        specifier.name = name;
        return specifier;
    }

    private String name;
    private Class<T> type;
    private Set<Annotation> qualifiers = Collections.emptySet();

    public ValueSpecifier() { }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public Set<Annotation> getQualifiers() {
        return Collections.unmodifiableSet(qualifiers);
    }

    public void setQualifiers(Annotation... qualifiers) {
        this.qualifiers = new LinkedHashSet<>(Arrays.asList(qualifiers));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("ValueSpecifier(%s, %s, %s)", type.getName(), name, qualifiers);
    }
}
