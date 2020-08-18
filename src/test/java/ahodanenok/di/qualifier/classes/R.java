package ahodanenok.di.qualifier.classes;

import javax.inject.Qualifier;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Repeatable(RC.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface R {
    String[] value();
}