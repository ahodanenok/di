package ahodanenok.di.value;

import ahodanenok.di.DIContainer;
import ahodanenok.di.value.metadata.ValueMetadata;

import javax.inject.Provider;

// todo: pre destroy
// todo: are pre destroy method inheritable?

// todo: some api to describe values (builders maybe), that they could be created by container lately
// todo: must have some sort of identifier by which it will be picked for providing dependency
public interface Value<T> {

    // todo: values after bind are considered as initialized and shouldn't be configurable after that
    // todo: validate values are correct (scopes, qualifiers, types, etc)
    default void bind(DIContainer container) { }

    Class<? extends T> type();

    ValueMetadata metadata();

    Provider<? extends T> provider();

    // todo: toString
}
