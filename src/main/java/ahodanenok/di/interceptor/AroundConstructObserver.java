package ahodanenok.di.interceptor;

public interface AroundConstructObserver {

    void observe(AroundConstruct<?> aroundConstruct) throws Exception;
}
