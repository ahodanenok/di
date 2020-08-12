package ahodanenok.di.interceptor.classes;

import ahodanenok.di.stereotype.Stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Stereotype
@StereotypeBindingA
@Retention(RetentionPolicy.RUNTIME)
public @interface StereotypeWithBinding { }
