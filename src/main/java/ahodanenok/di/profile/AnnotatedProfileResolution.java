package ahodanenok.di.profile;

import ahodanenok.di.exception.ConfigurationException;

import java.awt.font.TextHitInfo;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
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
