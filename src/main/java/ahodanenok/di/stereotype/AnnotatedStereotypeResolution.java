package ahodanenok.di.stereotype;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotatedStereotypeResolution implements StereotypeResolution {

    @Override
    public Set<Annotation> resolve(Class<?> clazz) {
        return resolveInternal(clazz);
    }

    @Override
    public Set<Annotation> resolve(Field field) {
        return resolveInternal(field);
    }

    @Override
    public Set<Annotation> resolve(Method method) {
        return resolveInternal(method);
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
