package ahodanenok.di.stereotype;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public final class StereotypeResolution {

    public Set<Annotation> resolve(AnnotatedElement element) {
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
