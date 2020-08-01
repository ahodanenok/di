package ahodanenok.di.name;

import javax.inject.Named;
import java.beans.Introspector;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

// todo: stereotype aware, if stereotype is present and named is not, check stereotype for name
public class AnnotatedNameResolution implements NameResolution {

    @Override
    public String resolve(Class<?> clazz) {
        return named(clazz, () -> {
            String name = clazz.getSimpleName();
            // todo: check how to use code points
            return Introspector.decapitalize(name);
        });
    }

    @Override
    public String resolve(Field field) {
        return named(field, field::getName);
    }

    @Override
    public String resolve(Method method) {
        return named(method, () -> {
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

    private String named(AnnotatedElement element, Supplier<String> defaultName) {
        Named named = element.getAnnotation(Named.class);
        // if there is no named annotation, element doesn't have a name, event a default one
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
