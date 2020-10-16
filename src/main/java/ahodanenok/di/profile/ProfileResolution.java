package ahodanenok.di.profile;

import ahodanenok.di.exception.ConfigurationException;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.StringJoiner;

// todo: profiles from stereotypes
public final class ProfileResolution {

    public String resolve(AnnotatedElement annotatedElement) {
        Profile profile = annotatedElement.getAnnotation(Profile.class);
        if (profile == null) {
            return null;
        }

        String[] profileConditionParts = profile.value();
        if (profileConditionParts.length == 0) {
            throwProfileExpressionIsEmpty(annotatedElement);
        }

        if (profileConditionParts.length == 1) {
            String p = profileConditionParts[0].trim();
            if (p.isEmpty()) {
                throwProfileExpressionIsEmpty(annotatedElement);
            }

            return p;
        }

        StringJoiner joiner = new StringJoiner("&");
        for (String part : profileConditionParts) {
            String p = part.trim();
            if (p.isEmpty()) {
                throwProfileExpressionIsEmpty(annotatedElement);
            }

            joiner.add("(" + p + ")");
        }

        return joiner.toString();
    }

    private void throwProfileExpressionIsEmpty(AnnotatedElement annotatedElement) {
        String msg = "@Profile annotation doesn't have any profiles" +
                " (or some profile expressions are empty) on the element " + annotatedElement;
        if (annotatedElement instanceof Member) {
            msg += " in the class ";
            msg += ((Member) annotatedElement).getDeclaringClass();
        }

        throw new ConfigurationException(msg);
    }
}
