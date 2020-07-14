package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;

public interface QualifierResolution {

    Annotation resolve(Class<?> clazz);

    Annotation resolve(Field field);

    Annotation resolve(Executable method);

    Annotation resolve(Executable method, int paramNum);
}
