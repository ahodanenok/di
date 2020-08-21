package ahodanenok.di.profile.classes;

import ahodanenok.di.profile.Profile;

public class FieldProfiles {

    @Profile("abc")
    String singleProfile;

    @Profile({" a & f", " !b", "c"})
    String multipleProfiles;
}
