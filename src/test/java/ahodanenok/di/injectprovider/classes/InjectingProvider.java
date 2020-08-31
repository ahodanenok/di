package ahodanenok.di.injectprovider.classes;

import javax.inject.Inject;
import javax.inject.Provider;

public class InjectingProvider {

    @Inject
    public Provider<String> provider;
}
