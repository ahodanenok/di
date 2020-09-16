package ahodanenok.di.value;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Primary { }
