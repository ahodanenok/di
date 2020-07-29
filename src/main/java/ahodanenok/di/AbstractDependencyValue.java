package ahodanenok.di;

public abstract class AbstractDependencyValue<T> implements DependencyValue<T> {

    private boolean defaultValue;

    public boolean isDefault() {
        return defaultValue;
    }

    public void setDefault(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}
