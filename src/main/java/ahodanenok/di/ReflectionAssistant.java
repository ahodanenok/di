package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReflectionAssistant {

    private ReflectionAssistant() { }

    public static Stream<Field> fields(Class<?> clazz) {
        if (clazz.getSuperclass() == null) {
            return Arrays.stream(clazz.getDeclaredFields());
        } else {
            return Stream.concat(fields(clazz.getSuperclass()), Arrays.stream(clazz.getDeclaredFields()));
        }
    }

    public static Stream<Method> methods(Class<?> clazz) {
        if (clazz.getSuperclass() == null) {
            return Arrays.stream(clazz.getDeclaredMethods());
        } else {
            return Stream.concat(methods(clazz.getSuperclass()), Arrays.stream(clazz.getDeclaredMethods()));
        }
    }

    public static Stream<? extends Annotation> parameterAnnotations(Executable executable, int parameterIndex, AnnotationPresence presence) {
        int parameterCount = executable.getParameterCount();

        if (parameterIndex < 0 || parameterIndex >= parameterCount) {
            throw new IllegalArgumentException(
                    String.format("executable has %d parameter(s), given parameter index: %d", executable.getParameterCount(), parameterIndex));
        }

        int parameterAnnotationsLength = executable.getParameterAnnotations().length;

        int index = parameterIndex;
        if (parameterCount > parameterAnnotationsLength) {
            index -= (parameterCount - parameterAnnotationsLength);
        }

        if (index < 0) {
            return Stream.empty();
        }

        if (presence == AnnotationPresence.DIRECTLY || presence == AnnotationPresence.PRESENT) {
            return Arrays.stream(executable.getParameterAnnotations()[index]);
        } else if (presence == AnnotationPresence.INDIRECTLY || presence == AnnotationPresence.ASSOCIATED) {
            Stream<? extends Annotation> stream = Stream.empty();
            Parameter p = executable.getParameters()[index];
            for (Class<? extends Annotation> type : annotationTypes(p.getDeclaredAnnotations())) {
                stream = Stream.concat(stream, Arrays.stream(p.getAnnotationsByType(type)));
            }

            return stream;
        } else {
            throw new IllegalArgumentException("unknown presence: " + presence);
        }
    }

    public enum AnnotationPresence {
        DIRECTLY,
        INDIRECTLY,
        PRESENT,
        ASSOCIATED
    }

    @SafeVarargs
    public static Stream<? extends Annotation> annotations(AnnotatedElement annotatedElement, AnnotationPresence presence, Class<? extends Annotation>... annotationTypes) {
        Stream<Annotation> stream = Stream.empty();
        if (presence == AnnotationPresence.DIRECTLY) {
            stream = Stream.concat(stream, Arrays.stream(annotatedElement.getDeclaredAnnotations()));
            if (annotationTypes.length > 0) {
                List<Class<?>> types = Arrays.asList(annotationTypes);
                stream = stream.filter(a -> types.contains(a.annotationType()));
            }
        } else if (presence == AnnotationPresence.PRESENT) {
            stream = Stream.concat(stream, Arrays.stream(annotatedElement.getAnnotations()));
            if (annotationTypes.length > 0) {
                List<Class<?>> types = Arrays.asList(annotationTypes);
                stream = stream.filter(a -> types.contains(a.annotationType()));
            }
        } else if (presence == AnnotationPresence.INDIRECTLY) {
            List<Class<? extends Annotation>> types;
            if (annotationTypes.length > 0) {
                types = Arrays.asList(annotationTypes);
            } else {
                types = annotationTypes(annotatedElement.getDeclaredAnnotations());
            }

            for (Class<? extends Annotation> type : types) {
                stream = Stream.concat(stream, Arrays.stream(annotatedElement.getDeclaredAnnotationsByType(type)));
            }
        } else if (presence == AnnotationPresence.ASSOCIATED) {
            List<Class<? extends Annotation>> types;
            if (annotationTypes.length > 0) {
                types = Arrays.asList(annotationTypes);
            } else {
                types = annotationTypes(annotatedElement.getAnnotations());
            }

            for (Class<? extends Annotation> type : types) {
                stream = Stream.concat(stream, Arrays.stream(annotatedElement.getAnnotationsByType(type)));
            }
        } else {
            throw new IllegalArgumentException("unknown presence: " + presence);
        }

        return stream;
    }

    private static List<Class<? extends Annotation>> annotationTypes(Annotation[] annotations) {
        return Arrays.stream(annotations).map(a -> {
            try {
                Method m = a.annotationType().getDeclaredMethod("value");
                // if annotation is a container for a repeatable annotation, then return type of the repeatable annotation
                if (m.getReturnType().isArray() && m.getReturnType().getComponentType().isAnnotation()
                        && m.getReturnType().getComponentType().isAnnotationPresent(Repeatable.class)) {
                    @SuppressWarnings("unchecked") // value() has array of annotations as return type
                            Class<? extends Annotation> c = (Class<? extends Annotation>) m.getReturnType().getComponentType();
                    return c;
                }
            } catch (NoSuchMethodException e) {
                // no-op
            }

            return a.annotationType();
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static Class<? extends Annotation> checkRepeatableContainer(Annotation a) {
        try {
            Method m = a.annotationType().getDeclaredMethod("value");
            // if annotation is a container for a repeatable annotation, then return type of the repeatable annotation
            if (m.getReturnType().isArray() && m.getReturnType().getComponentType().isAnnotation()
                    && m.getReturnType().getComponentType().isAnnotationPresent(Repeatable.class)) {
                @SuppressWarnings("unchecked") // value() has array of annotations as return type
                Class<? extends Annotation> c = (Class<? extends Annotation>) m.getReturnType().getComponentType();
                return c;
            }
        } catch (NoSuchMethodException e) {
            // no-op
        }

        return null;
    }

    private static Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS = new HashMap<>();
    static {
        PRIMITIVE_WRAPPERS.put(byte.class, Byte.class);
        PRIMITIVE_WRAPPERS.put(short.class, Short.class);
        PRIMITIVE_WRAPPERS.put(int.class, Integer.class);
        PRIMITIVE_WRAPPERS.put(long.class, Long.class);
        PRIMITIVE_WRAPPERS.put(float.class, Float.class);
        PRIMITIVE_WRAPPERS.put(double.class, Double.class);
        PRIMITIVE_WRAPPERS.put(boolean.class, Boolean.class);
        PRIMITIVE_WRAPPERS.put(char.class, Character.class);
    }

    public static Class<?> primitiveWrapperClass(Class<?> clazz) {
        Class<?> wrapper = PRIMITIVE_WRAPPERS.get(clazz);
        if (wrapper == null) {
            throw new IllegalArgumentException("wrapper not found for class " + clazz.getName());
        }

        return wrapper;
    }

    public static boolean isInstantiable(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return false;
        }

        if (clazz.isInterface()) {
            return false;
        }

        if (clazz.isAnnotation()) {
            return false;
        }

        if (clazz.isEnum()) {
            return false;
        }

        if (clazz.isArray()) {
            return false;
        }

        // todo: synthetic, anonymous

        return true;
    }
}
