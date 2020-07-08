package ahodanenok.di;

public abstract class AbstractDependencyValue<T> implements DependencyValue<T> {

    private Class<? super T> type;

    public AbstractDependencyValue(Class<? super T> type) {
        this.type = type;
    }

    public Class<? super T> getType() {
        return type;
    }
}
