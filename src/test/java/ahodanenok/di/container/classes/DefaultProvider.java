package ahodanenok.di.container.classes;

import ahodanenok.di.DefaultValue;

import javax.inject.Provider;

@DefaultValue
public class DefaultProvider implements Provider<String> {
    @Override
    public String get() {
        return "default";
    }
}
