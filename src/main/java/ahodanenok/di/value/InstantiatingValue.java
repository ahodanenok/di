package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.value.metadata.ClassMetadata;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class InstantiatingValue<T> extends AbstractValue<T> {

    private InjectableConstructor<? extends T> targetConstructor;

    public InstantiatingValue(Class<T> instanceClass) {
        this(instanceClass, instanceClass);
    }

    public InstantiatingValue(Class<T> type, Class<? extends T> instanceClass) {
        super(type, new ClassMetadata(instanceClass));

        if (!ReflectionAssistant.isInstantiable(instanceClass)) {
            throw new IllegalArgumentException("Can't create an instance of a class: " +  instanceClass.getName());
        }
    }

    @Override
    public Provider<? extends T> provider() {
        return () -> {
                if (targetConstructor == null) {
                    Constructor<? extends T> constructor = resolveConstructor();
                    targetConstructor = new InjectableConstructor<>(container, constructor);
//                    targetConstructor.setAroundConstructObserver();
                }

                T instance = targetConstructor.inject();
                if (instance != null) {
                    container.inject(instance);
                    // todo: post create
                }

                return instance;
        };
    }

    private Constructor<? extends T> resolveConstructor() {
        // todo: conform to spec
        // +Injectable constructors are annotated with @Inject and accept zero or more dependencies as arguments.
        // +@Inject can apply to at most one constructor per class.
        // @Inject is optional for public, no-argument constructors when no other constructors are present.
        // This enables injectors to invoke default constructors.

        Class<?> instanceClass = metadata().valueType();

        // Injectable constructors are annotated with @Inject
        Set<Constructor<?>> constructors = Arrays.stream(instanceClass.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());

        // @Inject can apply to at most one constructor per class.
        if (constructors.size() > 1) {
//            throw new DependencyInstantiatingException(id, instanceClass,
//                    "multiple constructors are marked with @Inject," +
//                    " to make this class available for dependency injection leave only such constructor," +
//                    " constructors: " + constructors);
            throw new RuntimeException();
        }

        Constructor<?> constructor = null;
        if (!constructors.isEmpty()) {
            @SuppressWarnings("unchecked") // constructor comes from instanceClass, which is T or its subclass
            Constructor<? extends T> c = (Constructor<? extends T>) constructors.iterator().next();
            constructor = c;
        } else {
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
//                throw new DependencyInstantiatingException(id, instanceClass, "default constructor is not found, mark appropriate constructor with @Inject" +
//                        " to make this class available for dependency injection, constructors: " + Arrays.asList(instanceClass.getDeclaredConstructors()));
                throw new RuntimeException();
            }

            // @Inject is optional for public, no-argument constructors when no other constructors are present.
            if (instanceClass.getDeclaredConstructors().length > 1) {
//                throw new DependencyInstantiatingException(id, instanceClass, "multiple constructors are found, mark appropriate constructor with @Inject" +
//                        " to make this class available for dependency injection, constructors: " + Arrays.asList(instanceClass.getDeclaredConstructors()));
                throw new RuntimeException();
            }
        }


        if (constructor == null) {
//            throw new DependencyInstantiatingException(id, instanceClass,
//                    "no applicable constructor found, either leave only one constructor without parameters or mark some constructor with @Inject");
            throw new RuntimeException();
        }


        return (Constructor<? extends T>) constructor;
    }
}