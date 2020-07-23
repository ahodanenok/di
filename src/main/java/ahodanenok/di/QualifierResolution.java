package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.Set;

public interface QualifierResolution {

    Set<Annotation> resolve(Class<?> clazz);

    Set<Annotation> resolve(Field field);

    Set<Annotation> resolve(Executable method);

    Set<Annotation> resolve(Executable method, int paramNum);
}
