package ahodanenok.di.stereotype;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public final class StereotypeResolution {

    /**
     * Returns stereotypes declared for a class
     */
    public Set<Annotation> resolve(Class<?> clazz) {
        return resolveInternal(clazz);
    }

    /**
     * Returns stereotypes declared for a field
     */
    public Set<Annotation> resolve(Field field) {
        return resolveInternal(field);
    }

    /**
     * Returns stereotypes declared for a method
     */
    public Set<Annotation> resolve(Method method) {
        return resolveInternal(method);
    }

    /**
     * Returns stereotypes declared for a constructor
     */
    public Set<Annotation> resolve(Constructor<?> constructor) {
        return resolveInternal(constructor);
    }

    private Set<Annotation> resolveInternal(AnnotatedElement element) {
        Set<Annotation> result = new HashSet<>();
        LinkedList<Annotation> queue = Arrays.stream(element.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(Stereotype.class))
                .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
        while (!queue.isEmpty()) {
            Annotation stereotype = queue.removeFirst();
            result.add(stereotype);

            for (Annotation s : stereotype.annotationType().getDeclaredAnnotations()) {
                if (s.annotationType().isAnnotationPresent(Stereotype.class) && !result.contains(s)) {
                    queue.addLast(s);
                }
            }
        }

        return result;
    }
}
