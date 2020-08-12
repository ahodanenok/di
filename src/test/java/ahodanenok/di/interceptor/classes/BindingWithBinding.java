package ahodanenok.di.interceptor.classes;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@InterceptorBinding
@BindingD
@Retention(RetentionPolicy.RUNTIME)
public @interface BindingWithBinding { }
