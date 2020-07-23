package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.util.*;

// todo: create annotation instance based on definition like in Annotation.toString (@com.acme.util.Name(first=Alfred, middle=E., last=Neuman))
// DependencyIdentifier.of(String.class, "@javax.inject.Named(value=Test)");

// todo: type + string, which is a name: @Named(name)
// DependencyIdentifier.of(String.class, "test"); -> DependencyIdentifier.of(String.class, @Named("test"));

// todo: type with wildcard type parameter is not valid
public final class DependencyIdentifier<T> {

    public static <T> DependencyIdentifier<T> of (Class<T> type) {
        return of(type, Collections.emptySet());
    }

    public static <T> DependencyIdentifier<T> of (Class<T> type, Set<Annotation> qualifiers) {
        return new DependencyIdentifier<>(type, qualifiers != null ? new LinkedHashSet<>(qualifiers) : Collections.emptySet());
    }

    public static <T> DependencyIdentifier<T> of (Class<T> type, Annotation... qualifiers) {
        return of(type, new LinkedHashSet<>(Arrays.asList(qualifiers)));
    }

    private Class<T> type;
    private Set<Annotation> qualifiers;

    public DependencyIdentifier(Class<T> type, Set<Annotation> qualifiers) {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }

        this.type = type;
        this.qualifiers = qualifiers;
    }

    public Class<T> type() {
        return type;
    }

    public Set<Annotation> qualifiers() {
        return Collections.unmodifiableSet(qualifiers);
    }

    @Override
    public String toString() {
        return String.format("DependencyID(%s, %s)", type.getName(), qualifiers);
    }

    @Override
    public int hashCode() {
        if (qualifiers != null) {
            return 31 * type.hashCode() + qualifiers.hashCode();
        } else {
            return type.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        DependencyIdentifier other = (DependencyIdentifier) obj;
        if (!Objects.equals(type, other.type)) {
            return false;
        }

        if (!Objects.equals(qualifiers, other.qualifiers)) {
            return false;
        }

        return true;
    }
}
