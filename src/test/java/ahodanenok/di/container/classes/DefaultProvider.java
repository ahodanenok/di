package ahodanenok.di.container.classes;

import ahodanenok.di.value.Default;

import javax.inject.Provider;

@Default
public class DefaultProvider implements Provider<String> {
    @Override
    public String get() {
        return "default";
    }
}
