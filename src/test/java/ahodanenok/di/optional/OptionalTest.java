package ahodanenok.di.optional;

import ahodanenok.di.*;
import ahodanenok.di.value.InstanceValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OptionalTest {

    @Test
    public void testOptionalMethodPrimitiveParameterMissing() {
        DIContainer container = DIContainer.builder().build();
        // todo: error type
        assertThrows(RuntimeException.class, () ->
                new InjectableMethod(container, Optionals.class.getDeclaredMethod("methodWithOptionalPrimitiveParameter", int.class))
                        .inject(new Optionals()));
    }

    @Test
    public void testOptionalMethodParameterMissing() throws Exception {
        DIContainer container = DIContainer.builder().build();
        Optionals obj = new Optionals();
        new InjectableMethod(container, Optionals.class.getDeclaredMethod("methodWithOptionalParameter", Integer.class)).inject(obj);
        assertNull(obj.checkInt);
    }

    @Test
    public void testOptionalMethodParameterWithNonOptional() throws Exception {
        DIContainer container = DIContainer.builder().addValue(new InstanceValue<Object>("check")).build();
        Optionals obj = new Optionals();
        new InjectableMethod(container, Optionals.class.getDeclaredMethod("methodWithOneOptionalAndNonOptionalParameters", String.class, Integer.class)).inject(obj);
        assertEquals("check", obj.checkStr);
        assertNull(obj.checkInt);
    }


    @Test
    public void testOptionalConstructorPrimitiveParameterMissing() {
        DIContainer container = DIContainer.builder().build();
        // todo: error type
        assertThrows(RuntimeException.class,
                () -> new InjectableConstructor<>(container, Optionals.class.getDeclaredConstructor(int.class)).inject());
    }

    @Test
    public void testOptionalConstructorParameterMissing() throws Exception {
        DIContainer container = DIContainer.builder().build();
        Optionals obj = new InjectableConstructor<>(container, Optionals.class.getDeclaredConstructor(Integer.class)).inject();
        assertNull(obj.checkInt);
    }

    @Test
    public void testOptionalConstructorParameterWithNonOptional() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstanceValue<>("check"))
                .build();
        Optionals obj = new InjectableConstructor<>(container, Optionals.class.getDeclaredConstructor(String.class, Integer.class)).inject();
        assertEquals("check", obj.checkStr);
        assertNull(obj.checkInt);
    }

    @Test
    public void testOptionalFieldPrimitiveMissing() throws Exception {
        DIContainer container = DIContainer.builder().build();
        // todo: error type
        assertThrows(RuntimeException.class,
                () -> new InjectableField(container, Optionals.class.getDeclaredField("optionalPrimitive")).inject(new Optionals()));
    }

    @Test
    public void testOptionalFieldMissing() throws Exception {
        DIContainer container = DIContainer.builder().build();
        Optionals obj = (Optionals) new InjectableField(container, Optionals.class.getDeclaredField("optional")).inject(new Optionals());
        assertNull(obj.optional);
    }
}
