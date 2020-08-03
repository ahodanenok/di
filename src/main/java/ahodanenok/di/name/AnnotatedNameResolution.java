package ahodanenok.di.name;

import javax.inject.Named;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

public class AnnotatedNameResolution implements NameResolution {

    @Override
    public String resolve(Class<?> clazz, Supplier<Set<Annotation>> stereotypes) {
        return named(clazz, stereotypes, () -> {
            String name = clazz.getSimpleName();
            // todo: check how to use code points
            return Introspector.decapitalize(name);
        });
    }

    @Override
    public String resolve(Field field, Supplier<Set<Annotation>> stereotypes) {
        return named(field, stereotypes, field::getName);
    }

    @Override
    public String resolve(Method method, Supplier<Set<Annotation>> stereotypes) {
        return named(method, stereotypes, () -> {
            String name = method.getName();
            if (name.length() > 2
                    && method.getReturnType().equals(boolean.class)
                    && name.startsWith("is")
                    && Character.isUpperCase(name.charAt(2))) {
                return Introspector.decapitalize(name.substring(2));
            } else if (name.length() > 3
                    && !method.getReturnType().equals(void.class)
                    && name.startsWith("get")
                    && Character.isUpperCase(name.charAt(3))) {
                return Introspector.decapitalize(name.substring(3));
            }

            return name;
        });
    }

    private String named(AnnotatedElement element, Supplier<Set<Annotation>> stereotypes, Supplier<String> defaultName) {
        Named named = element.getAnnotation(Named.class);
        if (named == null && stereotypes != null) {
            for (Annotation s : stereotypes.get()) {
                if (s.annotationType().isAnnotationPresent(Named.class)) {
                    Named n = s.annotationType().getAnnotation(Named.class);
                    if (!n.value().trim().isEmpty()) {
                        // todo: exception type, message
                        throw new IllegalStateException(
                                "@Named annotation declared on a stereotype can't provide a name," +
                                        " it can only be used on a stereotype to enable default name for values marked with this stereotype." +
                                        " Stereotype " + s + " with " + n);
                    }

                    // all @Named annotations will be with empty name, doesn't matter which will be assigned
                    named = n;
                }
            }
        }

        // if there is no named annotation, element doesn't have a name, even a default one
        if (named == null) {
            return null;
        }

        String name = named.value().trim();
        if (name.isEmpty()) {
            return defaultName.get();
        }

        return name;
    }
}
