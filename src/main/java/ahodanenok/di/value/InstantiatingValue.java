package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.event.AroundConstructEvent;
import ahodanenok.di.event.AroundProvisionEvent;
import ahodanenok.di.event.Events;
import ahodanenok.di.exception.ConfigurationException;
import ahodanenok.di.value.metadata.MutableValueMetadata;
import ahodanenok.di.value.metadata.ValueMetadata;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class InstantiatingValue<T> extends AbstractValue<T> {

    private final Class<? extends T> instanceClass;
    private InjectableConstructor<? extends T> targetConstructor;

    public InstantiatingValue(Class<T> instanceClass) {
        this(instanceClass, instanceClass);
    }

    public InstantiatingValue(Class<T> type, Class<? extends T> instanceClass) {
        super(type);
        this.instanceClass = instanceClass;

        if (!ReflectionAssistant.isInstantiable(instanceClass)) {
            throw new IllegalArgumentException("Can't create an instance of a class: " +  instanceClass.getName());
        }
    }

    @Override
    public Class<? extends T> realType() {
        return instanceClass;
    }

    @Override
    public void bind(DIContainer container) {
        super.bind(container);

        Events events = container.instance(Events.class);
        if (events == null) {
            throw new IllegalStateException();
        }

        Constructor<? extends T> constructor = resolveConstructor();
        targetConstructor = new InjectableConstructor<>(container, constructor);
        targetConstructor.onConstruct(ac -> events.fire(new AroundConstructEvent<>(this, ac)));
        targetConstructor.setOnProvision(ai -> events.fire(new AroundProvisionEvent(this, ai)));
    }

    @Override
    protected MutableValueMetadata resolveMetadata() {
        return container.instance(ValueMetadataResolution.class).resolve(realType());
    }

    @Override
    public Provider<? extends T> provider() {
        return () -> {
            T instance = targetConstructor.inject();
            // todo: log
            return instance;
        };
    }

    private Constructor<? extends T> resolveConstructor() {
        // todo: conform to spec
        // +Injectable constructors are annotated with @Inject and accept zero or more dependencies as arguments.
        // +@Inject can apply to at most one constructor per class.
        // @Inject is optional for public, no-argument constructors when no other constructors are present.
        // This enables injectors to invoke default constructors.

        Class<?> instanceClass = realType();

        // Injectable constructors are annotated with @Inject
        Set<Constructor<?>> constructors = Arrays.stream(instanceClass.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());

        // @Inject can apply to at most one constructor per class.
        if (constructors.size() > 1) {
            throw new ConfigurationException("Multiple constructors are marked with @Inject annotation in the class "
                    + instanceClass + ": " + constructors);
        }

        Constructor<?> constructor = null;
        if (!constructors.isEmpty()) {
            @SuppressWarnings("unchecked") // constructor comes from instanceClass, which is T or its subclass
            Constructor<? extends T> c = (Constructor<? extends T>) constructors.iterator().next();
            constructor = c;
        } else {
            // todo: if there is only one public constructor, pick it even if it isn't annotated with @Inject
            try {
                // @Inject is optional for public, no-argument constructors
                if (!Modifier.isStatic(instanceClass.getModifiers())
                        // todo: leave only support for member classes
                        && (instanceClass.isMemberClass() || instanceClass.isLocalClass() || instanceClass.isAnonymousClass())) {
                    constructor = instanceClass.getConstructor(instanceClass.getEnclosingClass());
                } else {
                    constructor = instanceClass.getConstructor();
                }
            } catch (NoSuchMethodException e) {
                // there is no default constructor
            }

            // @Inject is optional for public, no-argument constructors when no other constructors are present.
            if (instanceClass.getDeclaredConstructors().length > 1) {
                throw new ConfigurationException("Multiple constructors are found in the class "
                        + instanceClass + ", mark appropriate constructor with @Inject: "
                        + Arrays.toString(instanceClass.getDeclaredConstructors()));
            }
        }

        if (constructor == null) {
            throw new ConfigurationException("No applicable constructor found in the class " + instanceClass + ", either leave only one constructor without parameters or mark some constructor with @Inject");
        }

        // todo: suppress unchecked
        return (Constructor<? extends T>) constructor;
    }
}
