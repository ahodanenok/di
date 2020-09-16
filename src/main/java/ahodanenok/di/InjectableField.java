package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.interceptor.AroundProvision;
import ahodanenok.di.interceptor.InjectionPoint;
import ahodanenok.di.qualifier.QualifierResolution;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class InjectableField extends AbstractInjectable<Object> {

    private final Field field;

    public InjectableField(DIContainer container, Field field) {
        super(container);

        if (Modifier.isFinal(field.getModifiers())) {
            throw new InjectionFailedException(field, "field is final");
        }

        this.field = field;
    }

    @Override
    public Object inject(Object instance) {
        // todo: handle generics

        InjectionPoint injectionPoint = new InjectionPoint(field);
        injectionPoint.setQualifiers(container.instance(QualifierResolution.class).resolve(field));

        if (onProvision != null) {
            onProvision.accept(new AroundProvision(injectionPoint, value -> {
                if (value == null) {
                    value = resolveDependency(injectionPoint);
                }

                doInject(instance, value);
            }));
        } else {
            doInject(instance, resolveDependency(injectionPoint));
        }

        return instance;
    }

    private void doInject(Object instance, Object value) {
        try {
            ReflectionAssistant.fieldSet(field, instance, value);
        } catch (Exception e) {
            throw new InjectionFailedException(field, e);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + field.toGenericString() + ")";
    }
}
