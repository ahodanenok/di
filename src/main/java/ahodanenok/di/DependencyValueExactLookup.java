package ahodanenok.di;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Matches dependencies by equality of their types and qualifiers, types relationships are ignored.
 * Primitives and corresponding wrappers are considered identical.
 * Arrays are considered identical if element types are identical.
 */
public class DependencyValueExactLookup implements DependencyValueLookup {

    @Override
    public <T> Set<DependencyValue<T>> execute(Set<DependencyValue<?>> values, DependencyIdentifier<T> id) {
        Set<DependencyValue<T>> matching = new HashSet<>();
        for (DependencyValue<?> v : values) {
            if (v.id().qualifiers().isEmpty() == id.qualifiers().isEmpty()
                    && v.id().qualifiers().containsAll(id.qualifiers())
                    && typesMatch(id.type(), v.id().type())) {
                @SuppressWarnings("unchecked") // if ids are equals, type is T
                DependencyValue<T> matched = (DependencyValue<T>) v;
                matching.add(matched);
            }
        }

        return matching;
    }

    private boolean typesMatch(Class<?> a, Class<?> b) {
        if (a == b) {
            return true;
        }

        if (a.isPrimitive()) {
            a = ReflectionAssistant.primitiveWrapperClass(a);
        }

        if (b.isPrimitive()) {
            b = ReflectionAssistant.primitiveWrapperClass(b);
        }

        if (a == b) {
            return true;
        }

        return false;
    }
}
