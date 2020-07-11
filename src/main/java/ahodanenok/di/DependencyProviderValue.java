package ahodanenok.di;

import ahodanenok.di.annotation.DefaultScope;
import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import java.lang.annotation.Annotation;

public class DependencyProviderValue<T> extends AbstractDependencyValue<T> {

    private ReflectionAssistant reflectionAssistant;
    private Provider<? extends T> provider;
    private ScopeIdentifier scope;

    public DependencyProviderValue(Class<T> type, Provider<? extends T> provider) {
        super(type);
        this.provider = provider;
        this.reflectionAssistant = new ReflectionAssistant();
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return provider;
    }

    @Override
    public ScopeIdentifier scope() {
        if (scope == null) {
            Annotation scopeAnnotation = resolveScope(provider.getClass());
            if (scopeAnnotation != null) {
                scope = ScopeIdentifier.of(scopeAnnotation.annotationType());
            } else {
                scope = ScopeIdentifier.of(DefaultScope.class);
            }

        }

        return scope;
    }
}
