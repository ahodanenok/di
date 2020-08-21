package ahodanenok.di.profile.classes;

import ahodanenok.di.profile.Profile;

public class MethodProfiles {

    @Profile("abc")
    String singleProfile() {
        return null;
    }

    @Profile({" a & f", " !b", "c"})
    String multipleProfiles() {
        return null;
    }
}
