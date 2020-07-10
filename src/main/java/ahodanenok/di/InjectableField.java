package ahodanenok.di;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class InjectableField implements Injectable<Object> {

    private DIContainer container;
    private Field field;

    public InjectableField(DIContainer container, Field field) {
        this.container = container;
        this.field = field;
    }

    @Override
    public Object inject(Object instance) {
        // todo: handle generics
        // todo: set accessible

        // todo: conform to spec
        // Injectable fields:
        // are annotated with @Inject.
        // are not final.
        // may have any otherwise valid name.

        if (Modifier.isFinal(field.getModifiers())) {
            throw new RuntimeException("can't set final field");
        }

        Class<?> fieldType = field.getType();
        Object value = container.instance(fieldType);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            // todo: errors
            throw new RuntimeException(e);
        }

        return instance;
    }
}
