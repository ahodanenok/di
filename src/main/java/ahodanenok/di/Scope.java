package ahodanenok.di;

import java.util.function.Supplier;

public interface Scope {

    <T> T get(Class<T> type, Supplier<? extends T> supplier);
}
