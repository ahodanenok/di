package ahodanenok.di;

public abstract class AbstractDependencyValue<T> implements DependencyValue<T> {

    private DependencyIdentifier<T> id;

    public AbstractDependencyValue(DependencyIdentifier<T> id) {
        this.id = id;
    }

    public DependencyIdentifier<T> id() {
        return id;
    }
}
