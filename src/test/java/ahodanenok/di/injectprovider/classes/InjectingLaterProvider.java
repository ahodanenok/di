package ahodanenok.di.injectprovider.classes;

import ahodanenok.di.Later;

import javax.inject.Inject;
import javax.inject.Provider;

public class InjectingLaterProvider {

    @Inject
    @Later
    public Provider<String> provider;
}
