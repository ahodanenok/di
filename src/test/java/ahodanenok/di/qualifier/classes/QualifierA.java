package ahodanenok.di.qualifier.classes;

import javax.inject.Qualifier;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface QualifierA { }
