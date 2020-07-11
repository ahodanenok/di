package ahodanenok.di;

public abstract class AbstractDependencyValue<T> implements DependencyValue<T> {

    private Class<T> type;

    public AbstractDependencyValue(Class<T> type) {
        this.type = type;
    }

    public Class<T> type() {
        return type;
    }
}
