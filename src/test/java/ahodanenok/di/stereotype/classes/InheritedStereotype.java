package ahodanenok.di.stereotype.classes;

import ahodanenok.di.stereotype.Stereotype;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Stereotype
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritedStereotype { }
