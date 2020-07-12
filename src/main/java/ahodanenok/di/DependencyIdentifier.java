package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.util.Objects;

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
