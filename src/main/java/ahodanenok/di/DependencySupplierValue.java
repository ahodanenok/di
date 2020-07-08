package ahodanenok.di;

import java.util.function.Supplier;

public class DependencySupplierValue<T> extends AbstractDependencyValue<T> {

    private Supplier<T> supplier;

    public DependencySupplierValue(Class<? super T> type, Supplier<T> supplier) {
        super(type);
        this.supplier = supplier;
    }

    @Override
    public Supplier<T> supplier(DIContainer container) {
        return supplier;
    }
}
