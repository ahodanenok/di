package ahodanenok.di;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
// todo: target
public @interface Any { }
