package ahodanenok.di;

import ahodanenok.di.event.AroundConstructEvent;
import ahodanenok.di.event.AroundInjectEvent;
import ahodanenok.di.event.Event;
import ahodanenok.di.event.EventListener;
import ahodanenok.di.event.Events;
import ahodanenok.di.exception.UnknownScopeException;
import ahodanenok.di.interceptor.*;
import ahodanenok.di.name.AnnotatedNameResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.profile.ProfileMatcher;
import ahodanenok.di.scope.*;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;
import ahodanenok.di.util.Pair;
import ahodanenok.di.value.InstanceValue;
import ahodanenok.di.value.ProviderValue;
import ahodanenok.di.value.Value;
import ahodanenok.di.value.metadata.ValueMetadata;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

// todo: implement interceptors (https://jcp.org/en/jsr/detail?id=318) (https://docs.oracle.com/javaee/6/api/javax/interceptor/package-summary.html)
// todo: implement decorators
// todo: JSR-250
// todo: logging
// todo: events (like in cdi)
// todo: rethink exceptions' names
// todo: when null if valid as dependency value, when to throw unsatisfied dependency
// todo: documentation
// todo: generics
// todo: yaml config
// todo: @Priority
// todo: support injecting Optional
// todo: overriding meta-annotations attributes in annotated annotation

/**
 * Container is a coordinator between providers
 * Each of them manages some dependency which can be injected in other objects.
 */
public final class DIContainer {

    private Map<ScopeIdentifier, Scope> scopes;

    private Set<Value<?>> values;
    private Set<ManagedValue> managedValues;
    private ValueLookup valueLookup;

    private Set<Value<?>> interceptors;
    private InterceptorLookup interceptorLookup;

    private Map<Member, InterceptorChain> resolvedInterceptorChains;

    private InjectionPoint currentInjectionPoint;
    private LinkedList<InjectionPoint> injectionPoints;

    private DIContainer() {
        this.values = new HashSet<>();
        this.managedValues = new HashSet<>();
        this.interceptors = new HashSet<>();
        this.scopes = new HashMap<>();
        this.injectionPoints = new LinkedList<>();
    }

    private Scope lookupScope(ScopeIdentifier id) {
        Scope scope = scopes.get(id);
        if (scope != null) {
            return scope;
        } else {
            throw new UnknownScopeException(id);
        }
    }

    public Provider<?> provider(String name) {
        return provider(ValueSpecifier.named(name));
    }

    public <T> Provider<? extends T> provider(Class<T> type) {
        return provider(ValueSpecifier.of(type));
    }

    public <T> Provider<? extends T> provider(ValueSpecifier<T> id) {
        Value<T> value = chooseValue(valueLookup.execute(values, id));
        if (value == null) {
            return null;
        }

        return provider(value);
    }

    private <T> Value<T> chooseValue(Set<Value<T>> values) {
        if (values.isEmpty()) {
            return null;
        }

        if (values.size() == 1) {
            return values.iterator().next();
        }

        Set<Value<T>> primary = values.stream().filter(v -> v.metadata().isPrimary()).collect(Collectors.toSet());
        if (primary.size() > 1) {
            // todo: exception, message
            throw new IllegalStateException("multiple primary values");
        }

        if (primary.size() == 1) {
            return primary.iterator().next();
        }

        Set<Value<T>> withoutDefaults = values.stream().filter(v -> !v.metadata().isDefault()).collect(Collectors.toSet());
        if (withoutDefaults.size() > 1) {
            // todo: exception, message
            throw new IllegalStateException("multiple values matched");
            //throw new UnsatisfiedDependencyException(id, "multiple providers");
        }

        if (values.size() - withoutDefaults.size() > 1) {
            // todo: errors
            throw new IllegalStateException(
                    "There are multiple values marked as default: "
                            + values.stream().map(Value::type).collect(Collectors.toList()));
        }

        if (withoutDefaults.size() == 1) {
            return withoutDefaults.iterator().next();
        }

        if (withoutDefaults.size() != values.size()) {
            return values.stream().filter(v -> v.metadata().isDefault()).findFirst().orElse(null);
        }

        return null;
    }

    private <T> Provider<? extends T> provider(Value<T> value) {
        Objects.requireNonNull(value, "value is null")    ;
        Scope scope = lookupScope(value.metadata().getScope());
        return () -> {
            // InjectionPoint doesn't affect injection points stack,
            // because otherwise it will contain information about the place
            // where injection point being injected
            if (InjectionPoint.class.isAssignableFrom(value.type())) {
                return scope.get(value);
            }

            try {
                injectionPoints.push(currentInjectionPoint);
                return scope.get(value);
            } finally {
                injectionPoints.pop();
            }
        };
    }

    public <T> T instance(String name) {
        Provider<?> p = provider(ValueSpecifier.named(name));
        if (p == null) {
            return null;
        }

        @SuppressWarnings("unchecked") // no guarantees, lookup by name doesn't consider type
        T obj = (T) p.get();
        return obj;
    }

    public <T> T instance(Class<T> type) {
        Provider<? extends T> p = provider(ValueSpecifier.of(type));
        if (p == null) {
            return null;
        }

        return p.get();
    }

    public <T> T instance(ValueSpecifier<T> id) {
        Provider<? extends T> p = provider(id);
        if (p == null) {
            return null;
        }

        return p.get();
    }

    public void inject(Object instance) {
        inject(null, instance);
    }

    // todo: where to get value
    private void inject(Value<?> value, Object instance) {
        if (instance == null) {
            throw new IllegalArgumentException("instance is null");
        }

        // todo: Fields and methods in superclasses are injected before those in subclasses.
        // todo: check circular references
        ReflectionAssistant.fields(instance.getClass()).filter(f -> f.isAnnotationPresent(Inject.class)).forEach(f -> {
            // todo: cache
            // todo: which fields should be skipped (i.e inherited)
            InjectableField injectableField = new InjectableField(this, f);
            injectableField.setOnInject(ai -> interceptAroundInject(new AroundInjectEvent(value, ai)));
            injectableField.inject(instance);
        });

        // todo: conform to spec
        // A method annotated with @Inject that overrides another method annotated with @Inject will only be injected once per injection request per instance.
        // A method with no @Inject annotation that overrides a method annotated with @Inject will not be injected.
        ReflectionAssistant.methods(instance.getClass()).stream().filter(m -> m.isAnnotationPresent(Inject.class)).forEach(m -> {
            // todo: cache
            // todo: which methods should be skipped (i.e inherited)
            InjectableMethod injectableMethod = new InjectableMethod(this, m);
            injectableMethod.setOnInject(ai -> interceptAroundInject(new AroundInjectEvent(value, ai)));
            injectableMethod.inject(instance);
        });
    }

    private void handleEvent(Event event) {
        if (event instanceof AroundConstructEvent) {
            interceptAroundConstruct((AroundConstructEvent<?>) event);
        } else if (event instanceof AroundInjectEvent) {
            interceptAroundInject((AroundInjectEvent) event);
        }

        List<Pair<Value<?>, Method>> eventListeners = new ArrayList<>();
        for (ManagedValue v : managedValues) {
            for (Method m : v.getEventListeners()) {
                if (m.getParameterCount() != 1) {
                    // todo: error, msg
                    throw new IllegalStateException();
                }

                if (m.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                    eventListeners.add(new Pair<>(v, m));
                }
            }
        }

        // todo: sort event listeners according priority (and maybe some other conditions)
        for (Pair<Value<?>, Method> p : eventListeners) {
            try {
                p.getValue().invoke(provider(p.getKey()).get(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // todo: error, msg
                throw new RuntimeException(e);
            }
        }
    }

    private void interceptAroundInject(AroundInjectEvent event) {
        AroundInject aroundInject = event.getAroundInject();
        InjectionPoint injectionPoint = aroundInject.getInjectionPoint();
        assert event.getApplicant() != null;

        ValueSpecifier<?> specifier = ValueSpecifier.of(injectionPoint.getType(), injectionPoint.getQualifiers());
        // todo: @AroundInject interceptor?
        try {
            currentInjectionPoint = injectionPoint;
            aroundInject.setResolvedDependency(instance(specifier));
        } finally {
            currentInjectionPoint = null;
        }
        aroundInject.proceed();
    }

    private void interceptAroundConstruct(AroundConstructEvent<?> aroundConstructEvent) {
        if (aroundConstructEvent.getOwnerValue().metadata().isInterceptor() || interceptors.isEmpty()) {
            try {
                aroundConstructEvent.getAroundConstruct().proceed();
            } catch (ReflectiveOperationException e) {
                // todo: error
                throw new RuntimeException(e);
            }

            return;
        }

        if (resolvedInterceptorChains == null) {
            resolvedInterceptorChains = new HashMap<>();
        }

        InterceptorChain chain = resolvedInterceptorChains.get(aroundConstructEvent.getAroundConstruct().getConstructor());
        if (chain == null) {
            chain = new InterceptorChain(new InvocationContextImpl(aroundConstructEvent.getAroundConstruct()));
            List<Value<?>> classInterceptors = interceptorLookup.lookup(this, aroundConstructEvent.getOwnerValue(), interceptors);
            for (Value<?> interceptor : classInterceptors) {
                Method aroundConstructMethod = instance(InterceptorMetadataResolution.class)
                        .resolveAroundConstruct(interceptor.metadata().valueType());
                if (aroundConstructMethod != null) {
                    chain.add(ctx -> {
                        try {
                            return ReflectionAssistant.invoke(aroundConstructMethod, interceptor.provider().get(), ctx);
                        } catch (InvocationTargetException e) {
                            // todo: error, message
                            throw new RuntimeException(e);
                        }
                    });
                }
            }

            resolvedInterceptorChains.put(aroundConstructEvent.getAroundConstruct().getConstructor(), chain);
        }

        try {
            chain.proceed();
        } catch (Exception e) {
            // todo: error
            throw new RuntimeException(e);
        }
    }

    private class InjectionPointProvider implements Provider<InjectionPoint> {
        @Override
        public InjectionPoint get() {
            // todo: check not null and throw exception

            System.out.println("injectionPoints: " + injectionPoints);

            return injectionPoints.peek();
        }
    }

    private class ManagedValue implements Value<Object> {

        private Value<?> value;

        public ManagedValue(Value<?> value) {
            this.value = value;
        }

        @Override
        public Class<?> type() {
            return value.type();
        }

        @Override
        public ValueMetadata metadata() {
            return value.metadata();
        }

        @Override
        public void bind(DIContainer container) {
            value.bind(container);
        }

        @Override
        public Provider<?> provider() {
            return () -> {
                Object obj = value.provider().get();
                inject(value, obj);
                return obj;
            };
        }

        public List<Method> getEventListeners() {
            return Arrays.stream(value.metadata().valueType().getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(EventListener.class))
                    .collect(Collectors.toList());
        }
    }

    public static Builder builder() {
        DIContainer container = new DIContainer();
        return container.new Builder();
    }

    public class Builder {

        private List<Scope> scopes;
        private List<Value<?>> values;
        private String[] activeProfiles;

        public Builder addValue(Value<?> value) {
            if (values == null) {
                values = new ArrayList<>();
            }

            values.add(value);
            return this;
        }

        // todo: how to discover provider methods
//        public Builder scan(Class<?> clazz) {
//
//        }

        public Builder addScope(Scope scope) {
            if (scopes == null) {
                scopes = new ArrayList<>();
            }

            scopes.add(scope);
            return this;
        }

        /**
         * Provide a custom implementation of dependencies lookup.
         * Default implementation is {@link ValueExactLookup}
         */
        public Builder withValuesLookup(ValueLookup valueLookup) {
            DIContainer.this.valueLookup = valueLookup;
            return this;
        }

        /**
         * Provide a custom implementation of scope resolution.
         * Default implementation is {@link AnnotatedScopeResolution}
         */
        public Builder withScopeResolution(ScopeResolution scopeResolution) {
            //this.scopeResolution = scopeResolution; // todo
            return this;
        }

        public Builder withQualifierResolution(QualifierResolution qualifierResolution) {
            //this.qualifierResolution = qualifierResolution; // todo
            return this;
        }

        public Builder withNameResolution(NameResolution nameResolution) {
            //this.nameResolution = nameResolution; todo
            return this;
        }

        public Builder withStereotypeResolution(StereotypeResolution stereotypeResolution) {
            //this.stereotypeResolution = stereotypeResolution; todo
            return this;
        }

        public Builder activeProfiles(String... profiles) {
            this.activeProfiles = profiles;
            return this;
        }

        public DIContainer build() {
            DIContainer container = DIContainer.this;

            // custom scopes with the same identifier override built-in scopes
            Set<Scope> scopes = new HashSet<>();
            scopes.add(new DefaultScope());
            scopes.add(new SingletonScope());
            if (this.scopes != null) {
                scopes.addAll(this.scopes);
            }
            for (Scope scope : scopes) {
                container.scopes.putIfAbsent(scope.id(), scope);
            }

            if (container.valueLookup == null) {
                container.valueLookup = new ValueExactLookup();
            }

            if (container.interceptorLookup == null) {
                container.interceptorLookup = new DefaultInterceptorLookup();
            }

            InstanceValue<ScopeResolution> scopeResolution =
                    new InstanceValue<>(ScopeResolution.class, new AnnotatedScopeResolution());
            scopeResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            scopeResolution.metadata().setDefault(true);
            container.values.add(scopeResolution);

            InstanceValue<QualifierResolution> qualifierResolution =
                    new InstanceValue<>(QualifierResolution.class, new AnnotatedQualifierResolution());
            qualifierResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            qualifierResolution.metadata().setDefault(true);
            container.values.add(qualifierResolution);

            InstanceValue<NameResolution> nameResolution =
                    new InstanceValue<>(NameResolution.class, new AnnotatedNameResolution());
            nameResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            nameResolution.metadata().setDefault(true);
            container.values.add(nameResolution);

            InstanceValue<StereotypeResolution> stereotypeResolution =
                    new InstanceValue<>(StereotypeResolution.class, new AnnotatedStereotypeResolution());
            stereotypeResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            stereotypeResolution.metadata().setDefault(true);
            container.values.add(stereotypeResolution);

            InstanceValue<InterceptorMetadataResolution> interceptorMetadataResolution =
                    new InstanceValue<>(InterceptorMetadataResolution.class, new AnnotatedInterceptorMetadataResolution());
            interceptorMetadataResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            interceptorMetadataResolution.metadata().setDefault(true);
            container.values.add(interceptorMetadataResolution);

            ProviderValue<InjectionPoint> injectionPoint =
                    new ProviderValue<>(InjectionPoint.class, new InjectionPointProvider());
            container.values.add(injectionPoint);

            container.values.add(new InstanceValue<>(Events.class, new Events() {
                @Override
                public void fire(Event event) {
                    container.handleEvent(event);
                }
            }));

            if (activeProfiles == null) {
                activeProfiles = new String[0];
            }

            ProfileMatcher profileMatcher = new ProfileMatcher(new HashSet<>(Arrays.asList(activeProfiles)));

            if (values != null) {
                Set<ManagedValue> managedValues = new HashSet<>();
                for (Value<?> value : values) {
                    String valueProfiles = value.metadata().getProfilesCondition();
                    if (valueProfiles != null && !profileMatcher.matches(valueProfiles.trim())) {
                        continue;
                    }

                    value.bind(container);
                    if (value.metadata().isInterceptor()) {
                        container.interceptors.add(value);
                    }

                    managedValues.add(new ManagedValue(value));
                }

                container.values.addAll(managedValues);
                container.managedValues.addAll(managedValues);
            }

            // todo: eager init
//            for (Value<?> value : container.values) {
//                if (value.isInitOnStartup()) {
//                    if (value.scope().equals(ScopeIdentifier.of(Singleton.class))) {
//                        instance(value.id());
//                    } else {
//                        throw new IllegalStateException("Eager initialization is only applicable to singleton values");
//                    }
//                }
//            }

            return container;
        }
    }
}
