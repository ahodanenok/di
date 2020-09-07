package ahodanenok.di;

import ahodanenok.di.event.AroundConstructEvent;
import ahodanenok.di.event.AroundProvisionEvent;
import ahodanenok.di.event.Event;
import ahodanenok.di.event.EventListener;
import ahodanenok.di.event.Events;
import ahodanenok.di.exception.ConfigurationException;
import ahodanenok.di.exception.UnknownScopeException;
import ahodanenok.di.interceptor.*;
import ahodanenok.di.name.AnnotatedNameResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.profile.ProfileMatcher;
import ahodanenok.di.scope.*;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;
import ahodanenok.di.util.Pair;
import ahodanenok.di.value.*;
import ahodanenok.di.value.metadata.MutableValueMetadata;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

// todo: implement interceptors (https://jcp.org/en/jsr/detail?id=318) (https://docs.oracle.com/javaee/6/api/javax/interceptor/package-summary.html)
// todo: implement decorators
// todo: @Resource
// todo: logging
// todo: rethink exceptions' names
// todo: documentation
// todo: generics
// todo: yaml config
// todo: @Priority
// todo: overriding meta-annotations attributes in annotated annotation

/**
 * Container is a coordinator between providers
 * Each of them manages some dependency which can be injected in other objects.
 */
public final class DIContainer implements AutoCloseable {

    private Map<ScopeIdentifier, Scope> scopes;

    private List<Value<?>> values;
    private List<ManagedValueImpl> managedValues;
    private ValueLookup valueLookup;

    private List<Value<?>> interceptors;
    private InterceptorLookup interceptorLookup;

    private Map<Member, InterceptorChain> resolvedInterceptorChains;

    private InjectionPoint currentInjectionPoint;
    private LinkedList<InjectionPoint> injectionPoints;

    private DIContainer() {
        this.values = new ArrayList<>();
        this.managedValues = new ArrayList<>();
        this.interceptors = new ArrayList<>();
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
        Value<T> value = chooseValue(id, valueLookup.execute(values, id));
        if (value == null) {
            return null;
        }

        return provider(value);
    }

    private <T> Value<T> chooseValue(ValueSpecifier specifier, List<Value<T>> values) {
        if (values.isEmpty()) {
            return null;
        }

        if (values.size() == 1) {
            return values.get(0);
        }

        Set<Value<T>> primary = values.stream().filter(v -> v.metadata().isPrimary()).collect(Collectors.toSet());
        if (primary.size() > 1) {
            throw new ConfigurationException("Multiple values are marked as @PrimaryValue: "
                    + primary.stream().map(Value::type).collect(Collectors.toList()));
        }

        if (primary.size() == 1) {
            return primary.iterator().next();
        }

        Set<Value<T>> withoutDefaults = values.stream().filter(v -> !v.metadata().isDefault()).collect(Collectors.toSet());

        if (values.size() - withoutDefaults.size() > 1) {
            throw new ConfigurationException("Multiple values marked as @DefaultValue: "
                    + values.stream().map(Value::type).collect(Collectors.toList()));
        }

        if (withoutDefaults.size() == 1) {
            return withoutDefaults.iterator().next();
        }

        // if name is not specified, search for a value without name first
        if (specifier.getName() == null) {
            Set<Value<T>> unnamed = withoutDefaults.stream().filter(v -> v.metadata().getName() == null).collect(Collectors.toSet());
            if (unnamed.size() == 1) {
                return unnamed.iterator().next();
            }
        }

        // if qualifiers are not specified, search for an unqualified value first
        if (specifier.getQualifiers().isEmpty()) {
            Set<Value<T>> unqualified = withoutDefaults.stream().filter(v -> v.metadata().getQualifiers().isEmpty()).collect(Collectors.toSet());
            if (unqualified.size() == 1) {
                return unqualified.iterator().next();
            }
        }

        if (withoutDefaults.size() > 1) {
            throw new ConfigurationException(
                    "Multiple values are applicable for specifier " + specifier
                    + ", either make all except one default or make one a primary value:"
                    + withoutDefaults.stream().map(Value::type).collect(Collectors.toList()));
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

    private void injectStatic(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null");
        }

        Arrays.stream(clazz.getDeclaredFields()).filter(f -> Modifier.isStatic(f.getModifiers()) && f.isAnnotationPresent(Inject.class)).forEach(f -> {
            InjectableField injectableField = new InjectableField(this, f);
            injectableField.setOnProvision(ai -> interceptAroundInject(new AroundProvisionEvent(null, ai)));
            injectableField.inject(null);
        });

        Arrays.stream(clazz.getDeclaredMethods()).filter(m -> Modifier.isStatic(m.getModifiers()) && m.isAnnotationPresent(Inject.class)).forEach(m -> {
            InjectableMethod injectableMethod = new InjectableMethod(this, m);
            injectableMethod.setOnProvision(ai -> interceptAroundInject(new AroundProvisionEvent(null, ai)));
            injectableMethod.inject(null);
        });
    }

    // todo: cache
    // todo: Fields and methods in superclasses are injected before those in subclasses.
    // todo: check circular references
    private void inject(Value<?> value, Object instance) {

        // todo: conform to spec
        // A method annotated with @Inject that overrides another method annotated with @Inject will only be injected once per injection request per instance.
        // A method with no @Inject annotation that overrides a method annotated with @Inject will not be injected.

        if (instance == null) {
            throw new IllegalArgumentException("instance is null");
        }

        Map<Class<?>, List<Method>> methodsByClass = ReflectionAssistant.methods(instance.getClass())
                .stream()
                .filter(m -> !Modifier.isStatic(m.getModifiers()) && m.isAnnotationPresent(Inject.class))
                .collect(Collectors.groupingBy(Method::getDeclaringClass));

        for (Class<?> clazz : ReflectionAssistant.hierarchy(instance.getClass())) {
            Arrays.stream(clazz.getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers()) && f.isAnnotationPresent(Inject.class)).forEach(f -> {
                InjectableField injectableField = new InjectableField(this, f);
                injectableField.setOnProvision(ai -> interceptAroundInject(new AroundProvisionEvent(value, ai)));
                injectableField.inject(instance);
            });

            List<Method> methods = methodsByClass.get(clazz);
            if (methods != null) {
                for (Method m : methods) {
                    InjectableMethod injectableMethod = new InjectableMethod(this, m);
                    injectableMethod.setOnProvision(ai -> interceptAroundInject(new AroundProvisionEvent(value, ai)));
                    injectableMethod.inject(instance);
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        for (Scope scope :scopes.values()) {
            scope.destroy();
        }
    }

    private void handleEvent(Event event) {
        if (event instanceof AroundConstructEvent) {
            interceptAroundConstruct((AroundConstructEvent<?>) event);
        } else if (event instanceof AroundProvisionEvent) {
            interceptAroundInject((AroundProvisionEvent) event);
        }

        // todo: static listeners

        List<Pair<Value<?>, Method>> eventListeners = new ArrayList<>();
        for (ManagedValueImpl v : managedValues) {
            for (Method m : v.getEventListeners()) {
                if (m.getParameterCount() != 1) {
                    throw new ConfigurationException(
                            "Event listener method must accept a single parameter of event type: "
                            + m + " in the class " + m.getDeclaringClass());
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

    private void interceptAroundInject(AroundProvisionEvent event) {
        AroundProvision aroundProvision = event.getAroundProvision();
        InjectionPoint injectionPoint = aroundProvision.getInjectionPoint();

        if (!Modifier.isStatic(event.getAroundProvision().getInjectionPoint().getTarget().getModifiers()) && event.getApplicant() == null) {
            throw new IllegalStateException("value is null for non-static injection target");
        }

        // todo: @AroundInject interceptor?
        try {
            currentInjectionPoint = injectionPoint;
            aroundProvision.proceed();
        } finally {
            currentInjectionPoint = null;
        }
    }

    private void interceptAroundConstruct(AroundConstructEvent<?> aroundConstructEvent) {
        if (aroundConstructEvent.getOwnerValue().metadata().isInterceptor() || interceptors.isEmpty()) {
            aroundConstructEvent.getAroundConstruct().proceed();
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
                        .resolveAroundConstruct(interceptor.realType());
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

    private class ManagedValueImpl implements ManagedValue {

        private Value<?> value;

        public ManagedValueImpl(Value<?> value) {
            this.value = value;
        }

        @Override
        public Class<?> type() {
            return value.type();
        }

        @Override
        public Class<?> realType() {
            return value.realType();
        }

        @Override
        public MutableValueMetadata metadata() {
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

                Method postConstructMethod = getPostConstructMethod();
                if (postConstructMethod != null) {
                    try {
                        // todo: accessible
                        postConstructMethod.invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        // todo: error, message
                        throw new IllegalStateException(e);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        // If the method throws an unchecked exception the class MUST NOT be put into service.
                        // todo: throw exception?
                        return null;
                    }
                }

                return obj;
            };
        }

        @Override
        public List<Method> getEventListeners() {
            return Arrays.stream(value.realType().getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(EventListener.class))
                    .collect(Collectors.toList());
        }

        @Override
        public Method getPostConstructMethod() {
            List<Method> methods = ReflectionAssistant.methods(value.realType())
                    .stream()
                    .filter(m -> m.isAnnotationPresent(PostConstruct.class))
                    .collect(Collectors.toList());

            if (methods.isEmpty()) {
                return null;
            }

            if (methods.size() > 1) {
                throw new ConfigurationException(
                        "Multiple methods marked as @PostConstruct in the class " + value.realType() + ": " + methods);
            }

            Method postConstructMethod = methods.get(0);
            // todo: validate post construct method
            return postConstructMethod;
        }

        @Override
        public Method getPreDestroyMethod() {
            List<Method> methods = ReflectionAssistant.methods(value.realType())
                    .stream()
                    .filter(m -> m.isAnnotationPresent(PreDestroy.class))
                    .collect(Collectors.toList());

            if (methods.isEmpty()) {
                return null;
            }

            if (methods.size() > 1) {
                throw new ConfigurationException(
                        "Multiple methods marked as @PreDestroy in the class " + value.realType() + ": " + methods);
            }

            Method preDestroyMethod = methods.get(0);
            // todo: validate pre destroy method
            return preDestroyMethod;
        }

        // todo: around invoke
    }

    public static Builder builder() {
        DIContainer container = new DIContainer();
        return container.new Builder();
    }

    public class Builder {

        private List<Scope> scopes;
        private List<Value<?>> values;
        private String[] activeProfiles;
        private Set<Class<?>> injectStatic;

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

        // todo: @AllowStaticInject annotation to discover such permission automatically
        public Builder allowInjectStatic(Class<?>... classes) {
            if (injectStatic == null) {
                this.injectStatic = new HashSet<>();
            }

            this.injectStatic.addAll(Arrays.asList(classes));
            return this;
        }

        /**
         * Provide a custom implementation of dependencies lookup.
         * Default implementation is {@link DefaultValueLookup}
         */
        public Builder withValuesLookup(ValueLookup valueLookup) {
            DIContainer.this.valueLookup = valueLookup;
            return this;
        }

        public Builder withInterceptorLookup(InterceptorLookup interceptorLookup) {
            DIContainer.this.interceptorLookup = interceptorLookup;
            return this;
        }

        public Builder withActiveProfiles(String... profiles) {
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

            InstanceValue<DIContainer> containerValue = new InstanceValue<>(container);
            container.values.add(containerValue);

            if (container.valueLookup == null) {
                container.valueLookup = new DefaultValueLookup();
            }

            if (container.interceptorLookup == null) {
                container.interceptorLookup = new DefaultInterceptorLookup();
            }

            InstantiatingValue<ValueMetadataResolution> valueMetadataResolution =
                    new InstantiatingValue<>(ValueMetadataResolution.class, DefaultValueMetadataResolution.class);
            valueMetadataResolution.setResolveMetadata(false);
            valueMetadataResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            valueMetadataResolution.metadata().setDefault(true);
            container.values.add(new ManagedValueImpl(valueMetadataResolution));

            InstantiatingValue<ScopeResolution> scopeResolution =
                    new InstantiatingValue<>(ScopeResolution.class, AnnotatedScopeResolution.class);
            scopeResolution.setResolveMetadata(false);
            scopeResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            scopeResolution.metadata().setDefault(true);
            container.values.add(scopeResolution);

            InstantiatingValue<QualifierResolution> qualifierResolution =
                    new InstantiatingValue<>(QualifierResolution.class, AnnotatedQualifierResolution.class);
            qualifierResolution.setResolveMetadata(false);
            qualifierResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            qualifierResolution.metadata().setDefault(true);
            container.values.add(qualifierResolution);

            InstantiatingValue<NameResolution> nameResolution = new InstantiatingValue<>(NameResolution.class, AnnotatedNameResolution.class);
            nameResolution.setResolveMetadata(false);
            nameResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            nameResolution.metadata().setDefault(true);
            container.values.add(nameResolution);

            InstantiatingValue<StereotypeResolution> stereotypeResolution =
                    new InstantiatingValue<>(StereotypeResolution.class, AnnotatedStereotypeResolution.class);
            stereotypeResolution.setResolveMetadata(false);
            stereotypeResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            stereotypeResolution.metadata().setDefault(true);
            container.values.add(stereotypeResolution);

            InstantiatingValue<InterceptorMetadataResolution> interceptorMetadataResolution =
                    new InstantiatingValue<>(InterceptorMetadataResolution.class, AnnotatedInterceptorMetadataResolution.class);
            interceptorMetadataResolution.setResolveMetadata(false);
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

            // todo: collect all values and do bind (order by priority)
            for (Value<?> value : container.values) {
                value.bind(container);
            }

            if (values != null) {
                ProfileMatcher profileMatcher = new ProfileMatcher(new HashSet<>(Arrays.asList(activeProfiles)));
                Set<ManagedValueImpl> managedValues = new HashSet<>();
                for (Value<?> value : values) {
                    String valueProfiles = value.metadata().getProfilesCondition();
                    if (valueProfiles != null && !profileMatcher.matches(valueProfiles.trim())) {
                        continue;
                    }

                    value.bind(container);
                    if (value.metadata().isInterceptor()) {
                        container.interceptors.add(value);
                    }

                    if (value instanceof ManagedValue) {
                        managedValues.add((ManagedValueImpl) value);
                    } else {
                        managedValues.add(new ManagedValueImpl(value));
                    }
                }

                container.values.addAll(managedValues);
                container.managedValues.addAll(managedValues);
            }

            if (injectStatic != null) {
                Set<Class<?>> injected = new HashSet<>();
                for (Class<?> clazz : injectStatic) {
                    for (Class<?> h : ReflectionAssistant.hierarchy(clazz)) {
                        if (injected.add(h)) {
                            injectStatic(h);
                        }
                    }
                }
            }

            container.values.stream()
                    .filter(v -> v.metadata().isEager())
                    .sorted(Comparator.comparing(v -> v.metadata().getInitializationPhase()))
                    .forEach(v -> {
                        if (ScopeIdentifier.SINGLETON.equals(v.metadata().getScope())) {
                            provider(v).get();
                        } else {
                            throw new IllegalStateException("Eager initialization is only applicable to singleton values");
                        }
                    });

            return container;
        }
    }
}
