package ahodanenok.di.container.classes;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

@Singleton
public class ClassWithPreDestroy {

    public boolean destroyed;

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }
}
