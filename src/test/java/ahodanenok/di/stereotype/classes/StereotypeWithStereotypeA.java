package ahodanenok.di.stereotype.classes;

import ahodanenok.di.stereotype.Stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StereotypeWithStereotypeB
@Stereotype
@Retention(RetentionPolicy.RUNTIME)
public @interface StereotypeWithStereotypeA {}
