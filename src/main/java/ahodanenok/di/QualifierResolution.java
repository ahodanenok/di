package ahodanenok.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;

public interface QualifierResolution {

    <T extends Annotation> T resolve(Class<?> clazz);

    <T extends Annotation> T resolve(Field field);

    <T extends Annotation> T resolve(Executable method);

    <T extends Annotation> T resolve(Executable method, int paramNum);
}
