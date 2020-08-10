package ahodanenok.di;

import ahodanenok.di.event.Event;
import ahodanenok.di.event.Events;
import ahodanenok.di.value.Value;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Matches dependencies by equality of their types and qualifiers, types relationships are ignored.
 * Primitives and corresponding wrappers are considered identical.
 * Arrays are considered identical if element types are identical.
 */
public class ValueExactLookup implements ValueLookup {

    @Override
    public <T> Set<Value<T>> execute(Set<Value<?>> values, DependencyIdentifier<T> id) {
        Set<Value<T>> matching = new HashSet<>();
        for (Value<?> v : values) {
            if (v.metadata().getQualifiers().isEmpty() == id.qualifiers().isEmpty()
                    && (v.metadata().getQualifiers().containsAll(id.qualifiers()) || id.qualifiers().stream().anyMatch(a -> a.annotationType() == Any.class))
                    && typesMatch(id.type(), v.type())) {
                @SuppressWarnings("unchecked") // if ids are equals, type is T
                        Value<T> matched = (Value<T>) v;
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
