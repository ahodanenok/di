package ahodanenok.di;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public final class ReflectionAssistant {

    public Stream<Field> fields(Class<?> clazz) {
        if (clazz.getSuperclass() == null) {
            return Arrays.stream(clazz.getDeclaredFields());
        } else {
            return Stream.concat(fields(clazz.getSuperclass()), Arrays.stream(clazz.getDeclaredFields()));
        }
    }

    public Stream<Method> methods(Class<?> clazz) {
        if (clazz.getSuperclass() == null) {
            return Arrays.stream(clazz.getDeclaredMethods());
        } else {
            return Stream.concat(methods(clazz.getSuperclass()), Arrays.stream(clazz.getDeclaredMethods()));
        }
    }
}
