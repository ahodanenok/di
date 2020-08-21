package ahodanenok.di.profile;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.StringJoiner;

// todo: profiles from stereotypes
public class AnnotatedProfileResolution implements ProfileResolution{

    @Override
    public String resolve(Class<?> clazz) {
        return resolveFromElement(clazz);
    }

    @Override
    public String resolve(Field field) {
        return resolveFromElement(field);
    }

    @Override
    public String resolve(Method method) {
        return resolveFromElement(method);
    }

    private String resolveFromElement(AnnotatedElement annotatedElement) {
        Profile profile = annotatedElement.getAnnotation(Profile.class);
        if (profile == null) {
            return null;
        }

        String[] profileConditionParts = profile.value();
        if (profileConditionParts.length == 0) {
            // todo: exception, msg
            throw new IllegalStateException();
        }

        if (profileConditionParts.length == 1) {
            return normalize(profileConditionParts[0]);
        }

        StringJoiner joiner = new StringJoiner("&");
        for (String part : profileConditionParts) {
            joiner.add("(" + normalize(part) + ")");
        }

        return joiner.toString();
    }

    private String normalize(String profilePart) {
        String partTrimmed = profilePart.trim();
        if (partTrimmed.isEmpty()) {
            // todo: exception, msg
            throw new IllegalStateException();
        }

        return partTrimmed;
    }
}
