package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.util.Objects;

// todo: create annotation instance based on definition like in Annotation.toString (@com.acme.util.Name(first=Alfred, middle=E., last=Neuman))
// DependencyIdentifier.of(String.class, "@javax.inject.Named(value=Test)");

// todo: type + string, which is a name: @Named(name)
// DependencyIdentifier.of(String.class, "test"); -> DependencyIdentifier.of(String.class, @Named("test"));

// todo: type with wildcard type parameter is not valid
public final class DependencyIdentifier<T> {

    public static <T> DependencyIdentifier<T> of (Class<T> type) {
        return of(type, null);
    }

    public static <T> DependencyIdentifier<T> of (Class<T> type, Annotation qualifier) {
        return new DependencyIdentifier<>(type, qualifier);
    }

    private Class<T> type;
    private Annotation qualifier;

    public DependencyIdentifier(Class<T> type, Annotation qualifier) {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }

        this.type = type;
        this.qualifier = qualifier;
    }

    public Class<T> type() {
        return type;
    }

    public Annotation qualifier() {
        return qualifier;
    }

    @Override
    public String toString() {
        return String.format("DepID(%s, %s)", type.getName(), qualifier);
    }

    @Override
    public int hashCode() {
        if (qualifier != null) {
            return 31 * type.hashCode() + qualifier.hashCode();
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

        if (!Objects.equals(qualifier, other.qualifier)) {
            return false;
        }

        return true;
    }
}
