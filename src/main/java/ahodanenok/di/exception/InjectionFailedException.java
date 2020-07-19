package ahodanenok.di.exception;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;

public class InjectionFailedException extends DependencyInjectionException {

    public InjectionFailedException(Executable executable, Throwable cause) {
        super("Injection failed on " + executable.toGenericString(), cause);
    }

    public InjectionFailedException(Field field, Throwable cause) {
        super("Injection failed on " + field.toGenericString(), cause);
    }

    public InjectionFailedException(Field field, String cause) {
        super("Injection failed on " + field.toGenericString() + ": " + cause);
    }

    public InjectionFailedException(String message) {
        super(message);
    }
}
