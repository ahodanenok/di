package ahodanenok.di.profile;

import java.util.HashSet;
import java.util.Set;

// todo: implement profiles expression evaluation
public final class ProfileMatcher {

    private final Set<String> profiles;

    public ProfileMatcher(Set<String> profiles) {
        this.profiles = new HashSet<>(profiles);
    }

    public boolean matches(String profilesCondition) {
        System.out.println(profilesCondition + " - " + profiles + " " + profiles.contains(profilesCondition));

        return profiles.contains(profilesCondition);
    }

    private boolean not(String profilesCondition, int pos, int length) {
        return !profiles.contains(profilesCondition.substring(pos, length));
    }

    private boolean active(String profilesCondition, int pos, int length) {
        return profiles.contains(profilesCondition.substring(pos, length));
    }

    private boolean or(String profilesCondition, int pos, int length) {
        return true;
    }

    private boolean and(String profilesCondition, int pos, int length) {
        return true;
    }
}
