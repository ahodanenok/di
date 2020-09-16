package ahodanenok.di.interceptor;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class InterceptorChain {

    private final InvocationContext context;
    private final List<Function<InvocationContext, Object>> interceptors;

    public InterceptorChain(InvocationContext context) {
        this.context = context;
        this.interceptors = new ArrayList<>();
    }

    public void add(Function<InvocationContext, Object> interceptor) {
        interceptors.add(interceptor);
    }

    public void proceed() throws Exception {
        if (!interceptors.isEmpty()) {
            new ChainInvocationContext().proceed();
        } else {
            context.proceed();
        }
    }

    private class ChainInvocationContext implements InvocationContext {

        private int pos = 0;

        @Override
        public Object getTarget() {
            return context.getTarget();
        }

        @Override
        public Object getTimer() {
            return context.getTimer();
        }

        @Override
        public Method getMethod() {
            return context.getMethod();
        }

        @Override
        public Constructor<?> getConstructor() {
            return context.getConstructor();
        }

        @Override
        public Object[] getParameters() {
            return context.getParameters();
        }

        @Override
        public void setParameters(Object[] params) {
            context.setParameters(params);
        }

        @Override
        public Map<String, Object> getContextData() {
            return context.getContextData();
        }

        @Override
        public Object proceed() throws Exception {
            if (pos < interceptors.size()) {
                return interceptors.get(pos++).apply(this);
            } else {
                return context.proceed();
            }
        }
    }
}
