package ahodanenok.di.scope.classes;

import ahodanenok.di.stereotype.Stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@S1
@Stereotype
@Retention(RetentionPolicy.RUNTIME)
@interface StereotypeWithScope1 { }
