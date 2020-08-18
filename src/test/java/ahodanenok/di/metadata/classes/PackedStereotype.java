package ahodanenok.di.metadata.classes;

import ahodanenok.di.DefaultValue;
import ahodanenok.di.stereotype.Stereotype;

import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Stereotype
@DefaultValue
@Named
@Singleton
@Retention(RetentionPolicy.RUNTIME)
public @interface PackedStereotype { }
