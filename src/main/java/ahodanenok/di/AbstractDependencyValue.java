package ahodanenok.di;

import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractDependencyValue<T> implements DependencyValue<T> {

    private Class<T> type;

    public AbstractDependencyValue(Class<T> type) {
        this.type = type;
    }

    public Class<T> type() {
        return type;
    }


    // todo: option to make scope required
    // todo: where this method should pe placed
    protected Annotation resolveScope(Class<?> clazz) {
        Set<Annotation> scopes = Arrays.stream(clazz.getDeclaredAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(Scope.class))
                .collect(Collectors.toSet());

        // todo: errors

        if (scopes.size() > 1) {
            throw new RuntimeException("more than 1 scope");
        }

        if (scopes.size() == 1) {
            return scopes.iterator().next();
        } else {
            return null;
        }
    }
}
