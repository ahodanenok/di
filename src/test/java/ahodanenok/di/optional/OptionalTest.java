package ahodanenok.di.optional;

import ahodanenok.di.*;
import ahodanenok.di.value.InstanceValue;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
    public void testMethodWithJavaOptionalParameter() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstanceValue<>(5))
                .build();
        Optionals obj = new Optionals();
        new InjectableMethod(container, Optionals.class.getDeclaredMethod("methodWithJavaOptional", Optional.class)).inject(obj);
        assertNotNull(obj.checkOptional);
        assertTrue(obj.checkOptional.isPresent());
        assertEquals(5, obj.checkOptional.get());
    }

    @Test
    public void testMethodWithJavaOptionalParameterMissing() throws Exception {
        DIContainer container = DIContainer.builder().build();
        Optionals obj = new Optionals();
        new InjectableMethod(container, Optionals.class.getDeclaredMethod("methodWithJavaOptional", Optional.class)).inject(obj);
        assertNotNull(obj.checkOptional);
        assertFalse(obj.checkOptional.isPresent());
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
    public void testConstructorWithJavaOptionalParameter() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstanceValue<>(5))
                .build();
        Optionals obj = new InjectableConstructor<>(container, Optionals.class.getDeclaredConstructor(Optional.class)).inject();
        assertNotNull(obj.checkOptional);
        assertTrue(obj.checkOptional.isPresent());
        assertEquals(5, obj.checkOptional.get());
    }

    @Test
    public void testConstructorWithJavaOptionalParameterMissing() throws Exception {
        DIContainer container = DIContainer.builder().build();
        Optionals obj = new InjectableConstructor<>(container, Optionals.class.getDeclaredConstructor(Optional.class)).inject();
        assertNotNull(obj.checkOptional);
        assertFalse(obj.checkOptional.isPresent());
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

    @Test
    public void testJavaOptionalField() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstanceValue<>(5))
                .build();
        Optionals obj = (Optionals) new InjectableField(container, Optionals.class.getDeclaredField("javaOptional")).inject(new Optionals());
        assertNotNull(obj.javaOptional);
        assertTrue(obj.javaOptional.isPresent());
        assertEquals(5, obj.javaOptional.get());
    }

    @Test
    public void testJavaOptionalFieldMissing() throws Exception {
        DIContainer container = DIContainer.builder().build();
        Optionals obj = (Optionals) new InjectableField(container, Optionals.class.getDeclaredField("javaOptional")).inject(new Optionals());
        assertNotNull(obj.javaOptional);
        assertFalse(obj.javaOptional.isPresent());
    }
}
