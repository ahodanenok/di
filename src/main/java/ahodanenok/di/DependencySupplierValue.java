package ahodanenok.di;

import java.util.function.Supplier;

public class DependencySupplierValue<T> extends AbstractDependencyValue<T> {

    private Supplier<T> supplier;

    public DependencySupplierValue(Class<T> type, Supplier<T> supplier) {
        super(type);
        this.supplier = supplier;
    }

    @Override
    public Supplier<? extends T> supplier(DIContainer container) {
        return supplier;
    }
}
