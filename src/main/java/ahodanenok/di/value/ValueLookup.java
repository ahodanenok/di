package ahodanenok.di.value;

import ahodanenok.di.qualifier.Any;
import ahodanenok.di.ReflectionAssistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Matches dependencies by equality of their types and qualifiers, types relationships are ignored.
 * Primitives and corresponding wrappers are considered identical.
 * Arrays are considered identical if element types are identical.
 */
public final class ValueLookup {

    /**
     * Collect all dependencies matching given id.
     * Container will ensure that neither values nor id are nulls.
     *
     * @return matched dependencies or empty set if none found
     */
    public <T> List<Value<T>> execute(List<? extends Value<?>> values, ValueSpecifier<T> specifier) {
        List<Value<T>> matching = new ArrayList<>();
        for (Value<?> v : values) {
            if (specifier.getName() != null && !specifier.getName().equals(v.metadata().getName())) {
                continue;
            }

            if (specifier.getType() != null && !ReflectionAssistant.typesMatch(specifier.getType(), v.type())) {
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
}
