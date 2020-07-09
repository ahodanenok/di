package ahodanenok.di;

public abstract class AbstractDependencyValue<T> implements DependencyValue<T> {

    private Class<T> type;
    private String scope = "default";

    public AbstractDependencyValue(Class<T> type) {
        this.type = type;
    }

    public Class<T> type() {
        return type;
    }

    // todo: use builder
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String scope() {
        return scope;
    }
}
