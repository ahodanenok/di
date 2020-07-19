package ahodanenok.di;

import java.util.Set;

/**
 * Behaviour for determining how dependencies are selected by id from all available.
 * @see DIContainer.Builder#withValuesLookup(ahodanenok.di.DependencyValueLookup)
 */
public interface DependencyValueLookup {

    /**
     * Collect all dependencies matching given id.
     * Container will ensure that neither values nor id are nulls.
     *
     * @return matched dependencies or empty set if none found
     */
    <T> Set<DependencyValue<T>> execute(Set<DependencyValue<?>> values, DependencyIdentifier<T> id);
}
