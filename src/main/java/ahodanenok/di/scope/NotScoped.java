package ahodanenok.di.scope;

import javax.inject.Scope;
import java.lang.annotation.*;

/**
 * Dependencies with this scope are created every time they requested.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD}) // todo: field
@Documented
public @interface NotScoped { }
