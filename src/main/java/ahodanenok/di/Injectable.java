package ahodanenok.di;

//todo: remove default method
public interface Injectable<T> {

    T inject(T instance);

    default T inject() {
        return inject(null);
    }
}
