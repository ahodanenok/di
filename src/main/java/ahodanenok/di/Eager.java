package ahodanenok.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tells to container to initialize value at container initialization
 * Applicable only to {@link javax.inject.Singleton singleton} beans
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Eager { }
