package ahodanenok.di.scope;

import javax.inject.Scope;
import java.lang.annotation.*;

/**
 * Dependencies with this scope are created every time they requested.
 */
@Scope
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotScoped { }
