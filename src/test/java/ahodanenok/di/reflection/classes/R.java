package ahodanenok.di.reflection.classes;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Repeatable(RC.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface R {
    String value();
}
