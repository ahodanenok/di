package ahodanenok.di.interceptor;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.function.Function;

public final class AroundConstruct<T> {

    private Constructor<? extends T> constructor;
    private Object[] args;
    private T instance;

    private Function<Object[], T> proceedFunction;

    public AroundConstruct(Constructor<? extends T> constructor, Object[] args, Function<Object[], T> proceedFunction) {
        this.constructor = constructor;
        this.args = args;
        this.proceedFunction = proceedFunction;
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

    public T proceed() {
        if (instance != null) {
            return instance;
        }

        instance = proceedFunction.apply(args);
        return instance;
    }
}
