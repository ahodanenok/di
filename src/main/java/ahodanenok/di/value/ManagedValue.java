package ahodanenok.di.value;

import java.lang.reflect.Method;
import java.util.List;

public interface ManagedValue extends Value<Object> {

    List<Method> getEventListeners();

    Method getPostConstructMethod();

    Method getPreDestroyMethod();
}
