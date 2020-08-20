package ahodanenok.di;

import ahodanenok.di.value.Value;

import java.util.Set;

/**
 * Behaviour for determining how dependencies are selected by id from all available.
 * @see DIContainer.Builder#withValuesLookup(ValueLookup)
 */
public interface ValueLookup {

    /**
     * Collect all dependencies matching given id.
     * Container will ensure that neither values nor id are nulls.
     *
     * @return matched dependencies or empty set if none found
     */
    <T> Set<Value<T>> execute(Set<Value<?>> values, ValueSpecifier<T> id);
}
