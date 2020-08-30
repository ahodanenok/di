package ahodanenok.di;

import java.lang.annotation.*;

/**
 * Tells to container to initialize value at container initialization
 * Applicable only to {@link javax.inject.Singleton singleton} beans
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Eager {

    /**
     * Defines a phase during which eager value should be initialized
     * Values with a lower phase initialized before values with a higher phase
     */
    int phase() default 0;
}
