package ahodanenok.di;

import java.util.function.Supplier;

public class DefaultScope implements Scope {

    @Override
    public <T> T get(Class<T> type, Supplier<? extends T> supplier) {
        return supplier.get();
    }
}
