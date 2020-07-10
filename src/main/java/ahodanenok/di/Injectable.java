package ahodanenok.di;

public interface Injectable<T> {

    T inject(T instance);

    default T inject() {
        return inject(null);
    }
}
