package ahodanenok.di.interceptor;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public final class InvocationContextImpl implements InvocationContext {

    private AroundConstruct<?> aroundConstruct;

    public InvocationContextImpl(AroundConstruct<?> aroundConstruct) {
        this.aroundConstruct = aroundConstruct;
    }

    @Override
    public Object getTarget() {
        return aroundConstruct.getInstance();
    }

    @Override
    public Object getTimer() {
        return null;
    }

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public Constructor<?> getConstructor() {
        return aroundConstruct.getConstructor();
    }

    @Override
    public Object[] getParameters() {
        if (aroundConstruct == null) {
            throw new IllegalStateException("Not AroundConstruct callback");
        }

        return aroundConstruct.getArgs();
    }

    @Override
    public void setParameters(Object[] params) {
        if (aroundConstruct == null) {
            throw new IllegalStateException("Not AroundConstruct callback");
        }

        this.aroundConstruct.setArgs(params);
    }

    @Override
    public Map<String, Object> getContextData() {
        return Collections.emptyMap();
    }

    @Override
    public Object proceed() throws Exception {
        if (aroundConstruct != null) {
            return aroundConstruct.proceed();
        }

        throw new IllegalStateException();
    }
}
