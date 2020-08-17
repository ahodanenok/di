package ahodanenok.di.value.metadata;

import ahodanenok.di.*;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;

import java.lang.reflect.Method;
import java.util.Collections;

public final class MethodMetadata extends ValueMetadata implements ResolvableMetadata {

    private final Method method;

    public MethodMetadata(Class<?> type, Method method) {
        super(type);
        this.method = method;
    }

    @Override
    public void resolve(DIContainer container) {
        stereotypes = container.instance(StereotypeResolution.class).resolve(method);
        name = container.instance(NameResolution.class).resolve(method, this::getStereotypes);
        qualifiers = container.instance(QualifierResolution.class).resolve(method);
        scope = container.instance(ScopeResolution.class).resolve(method, this::getStereotypes, ScopeIdentifier.NOT_SCOPED);
        isDefault = method.isAnnotationPresent(DefaultValue.class)
                || stereotypes.stream().anyMatch(s -> s.annotationType().isAnnotationPresent(DefaultValue.class));
        eager = method.isAnnotationPresent(Eager.class);

        // todo: interceptorBindings
        interceptorBindings = Collections.emptySet();
    }
}
