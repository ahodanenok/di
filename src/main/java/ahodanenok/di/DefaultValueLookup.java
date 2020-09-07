package ahodanenok.di;

import ahodanenok.di.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matches dependencies by equality of their types and qualifiers, types relationships are ignored.
 * Primitives and corresponding wrappers are considered identical.
 * Arrays are considered identical if element types are identical.
 */
public class DefaultValueLookup implements ValueLookup {

    @Override
    public <T> List<Value<T>> execute(List<Value<?>> values, ValueSpecifier<T> specifier) {
        List<Value<T>> matching = new ArrayList<>();
        for (Value<?> v : values) {
            if (specifier.getName() != null && !specifier.getName().equals(v.metadata().getName())) {
                continue;
            }

            if (specifier.getType() != null && !typesMatch(specifier.getType(), v.type())) {
                continue;
            }

            boolean matched = false;
            if (specifier.getQualifiers().isEmpty()) {
                matched = true;
            } else if (specifier.getQualifiers().stream().anyMatch(a -> a.annotationType() == Any.class)) {
                matched = true;
            } else if (!v.metadata().getQualifiers().isEmpty()
                    && v.metadata().getQualifiers().containsAll(specifier.getQualifiers())) {
                matched = true;
            }

            if (matched) {
                @SuppressWarnings("unchecked") // if types match, type is T
                Value<T> vv = (Value<T>) v;
                matching.add(vv);
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
