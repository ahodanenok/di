package ahodanenok.di;

public abstract class AbstractDependencyValue<T> implements DependencyValue<T> {

    protected Boolean initOnStartup;
    private boolean defaultValue;

    public boolean isDefault() {
        return defaultValue;
    }

    public void setDefault(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setInitOnStartup(boolean initOnStartup) {
        this.initOnStartup = initOnStartup;
    }

    public boolean isInitOnStartup() {
        return Boolean.TRUE.equals(initOnStartup);
    }
}
