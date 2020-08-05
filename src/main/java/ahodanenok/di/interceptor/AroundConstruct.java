package ahodanenok.di.interceptor;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public final class AroundConstruct<T> {

    private Constructor<? extends T> constructor;
    private Object[] args;
    private T instance;

    public AroundConstruct(Constructor<? extends T> constructor, Object[] args) {
        this.constructor = constructor;
        this.args = args;
    }

    public Constructor<? extends T> getConstructor() {
        return constructor;
    }

    public Object[] getArgs() {
        return Arrays.copyOf(args, args.length);
    }

    public void setArgs(Object[] args) {
        if (args.length != constructor.getParameterCount()) {
            throw new IllegalArgumentException("number of parameters must be " + constructor.getParameterCount());
        }
        // todo: IllegalArgumentException if the types of the given parameter values do not match the types of the method or constructor parameters

        this.args = Arrays.copyOf(args, args.length);
    }

    public T getInstance() {
        return instance;
    }

    public T proceed() throws ReflectiveOperationException {
        boolean accessible = constructor.isAccessible();
        try {
            constructor.setAccessible(true);
            instance = constructor.newInstance(args);
            return instance;
        } finally {
            constructor.setAccessible(accessible);
        }
    }
}
