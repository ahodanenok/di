package ahodanenok.di.name;

import ahodanenok.di.Later;
import ahodanenok.di.exception.ConfigurationException;
import ahodanenok.di.stereotype.StereotypeResolution;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

// todo: move default name generation to a separate strategy
public class NameResolution {

    private final Provider<StereotypeResolution> stereotypeResolution;

    @Inject
    public NameResolution(@Later Provider<StereotypeResolution> stereotypeResolution) {
        this.stereotypeResolution = stereotypeResolution;
    }

    public String resolve(Class<?> clazz) {
        return named(clazz, () -> stereotypeResolution.get().resolve(clazz), () -> {
            String name = clazz.getSimpleName();
            // todo: check how to use code points
            return Introspector.decapitalize(name);
        });
    }

    public String resolve(Field field) {
        return named(field, () -> stereotypeResolution.get().resolve(field), field::getName);
    }

    public String resolve(Method method) {
        return named(method, () -> stereotypeResolution.get().resolve(method), () -> {
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
                        throw new ConfigurationException(
                                "@Named annotation declared on a stereotype can't provide a name," +
                                        " it can only be used on a stereotype to enable default name for values marked with this stereotype." +
                                        " Stereotype " + s + " with " + n);
                    }

                    // all @Named annotations will be with empty name, doesn't matter which will be assigned
                    named = n;
                }
            }
        }

        // if there is no Named or ManagedBean annotations, element doesn't have a name, even a default one

        String name = null;
        if (named != null) {
            name = named.value().trim();
        }

        ManagedBean managedBean = element.getAnnotation(ManagedBean.class);
        if (managedBean != null) {
            String managedBeanName = managedBean.value().trim();
            if (name == null || name.isEmpty()) {
                name = managedBeanName;
            } else if (!managedBeanName.isEmpty() && !Objects.equals(name, managedBeanName)) {
                throw new ConfigurationException("@Named and @ManagedBean provide different names for value: " + name + ", " + managedBean);
            }
        }

        // todo: infer name from @Resource annotation? when declared on a class

        if (name != null && name.isEmpty()) {
            return defaultName.get();
        }

        return name;
    }
}
