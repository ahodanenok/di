package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReflectionAssistant {

    private ReflectionAssistant() { }

    public static List<Class<?>> hierarchy(Class<?> clazz) {
        LinkedList<Class<?>> classes = new LinkedList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            classes.addFirst(currentClass);
            currentClass = currentClass.getSuperclass();
        }

        return classes;
    }

    public static List<Field> fields(Class<?> clazz) {
        LinkedList<Field> fields = new LinkedList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            for (Field f : currentClass.getDeclaredFields()) {
                fields.addFirst(f);
            }

            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    public static List<Method> methods(Class<?> clazz) {
        Set<MethodKey> keys = new HashSet<>();
        LinkedList<Method> methods = new LinkedList<>();

        Class<?> currentClass = clazz;
        while (currentClass != null) {
            for (Method m : currentClass.getDeclaredMethods()) {
                if (Modifier.isStatic(m.getModifiers()) || Modifier.isPrivate(m.getModifiers())) {
                    methods.addFirst(m);
                } else if (keys.add(new MethodKey(m))) {
                    methods.addFirst(m);
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return methods;
    }

    public static boolean isPackagePrivate(int modifiers) {
        return !Modifier.isProtected(modifiers) && !Modifier.isPrivate(modifiers) && !Modifier.isPublic(modifiers);
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
            Set<Class<? extends Annotation>> types;
            if (annotationTypes.length > 0) {
                types = new HashSet<>(Arrays.asList(annotationTypes));
            } else {
                types = annotationTypes(annotatedElement.getDeclaredAnnotations());
            }

            for (Class<? extends Annotation> type : types) {
                stream = Stream.concat(stream, Arrays.stream(annotatedElement.getDeclaredAnnotationsByType(type)));
            }
        } else if (presence == AnnotationPresence.ASSOCIATED) {
            Set<Class<? extends Annotation>> types;
            if (annotationTypes.length > 0) {
                types = new HashSet<>(Arrays.asList(annotationTypes));
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

    private static Set<Class<? extends Annotation>> annotationTypes(Annotation[] annotations) {
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
        }).filter(Objects::nonNull).collect(Collectors.toSet());
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

    public static void checkFieldTypeAssignable(Class<?> type, Field field) {
        Class<?> target;
        if (type.isPrimitive()) {
            target = PRIMITIVE_WRAPPERS.get(type);
        } else {
            target = type;
        }

        Class<?> src;
        if (field.getType().isPrimitive()) {
            src = PRIMITIVE_WRAPPERS.get(field.getType());
        } else {
            src = field.getType();
        }

        if (!target.isAssignableFrom(src)) {
            throw new IllegalArgumentException(String.format("Type of the field %s is not assignable to %s", field, type));
        }
    }

    public static Object invoke(Method m, Object instance, Object... args) throws InvocationTargetException {
        boolean accessible = m.isAccessible();
        try {
            m.setAccessible(true);
            return m.invoke(instance, args);
        } catch (IllegalAccessException e) {
            // todo: could be fired if accessible is true?
            throw new IllegalStateException(e);
        } finally {
            m.setAccessible(accessible);
        }
    }

    private static class MethodKey {

        private Method method;

        public MethodKey(Method method) {
            this.method = method;
        }

        @Override
        public int hashCode() {
            return method.getName().hashCode() * 31 + Arrays.hashCode(method.getParameterTypes()) + (isPackagePrivate(method.getModifiers()) ? 1 : 0);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            MethodKey other = (MethodKey) obj;
            if (!methodsEqual(this.method, other.method)) {
                return false;
            }

            if (isPackagePrivate(method.getModifiers()) && isPackagePrivate(other.method.getModifiers())) {
                return method.getDeclaringClass().getPackage().getName().equals(other.method.getDeclaringClass().getPackage().getName());
            }

            return true;
        }
    }

    private static boolean methodsEqual(Method a, Method b) {
        return a.getName().equals(b.getName()) && Arrays.equals(a.getParameterTypes(), b.getParameterTypes());
    }
}
