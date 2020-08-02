package ahodanenok.di.stereotype;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Inherited
@Retention(RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Stereotype { }
