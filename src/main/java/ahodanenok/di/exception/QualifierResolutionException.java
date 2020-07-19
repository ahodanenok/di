package ahodanenok.di.exception;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;

public class QualifierResolutionException extends DependencyInjectionException {


    public QualifierResolutionException(Class<?> clazz, String reason) {
        super(String.format("Couldn't resolve qualifiers on '%s': %s", clazz.getSimpleName(), reason));
    }

    public QualifierResolutionException(Field field, String reason) {
        super(String.format("Couldn't resolve qualifiers on '%s': %s", field.toGenericString(), reason));
    }

    public QualifierResolutionException(Executable executable, String reason) {
        super(String.format("Couldn't resolve qualifiers on '%s': %s", executable.toGenericString(), reason));
    }

    public QualifierResolutionException(Executable executable, int paramNum, String reason) {
        super(String.format("Couldn't resolve qualifiers for parameter %d on '%s': %s", paramNum, executable.toGenericString(), reason));
    }
}
