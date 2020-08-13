package ahodanenok.di.name.classes;

import ahodanenok.di.stereotype.Stereotype;

import javax.inject.Named;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Stereotype
@Named
@Retention(RetentionPolicy.RUNTIME)
@interface DefaultNamedStereotype { }
