package ahodanenok.di;

public abstract class AbstractDependencyValue<T> implements DependencyValue<T> {

    protected String name;
    protected Boolean initOnStartup;
    protected Boolean defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefault() {
        return Boolean.TRUE.equals(defaultValue);
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
