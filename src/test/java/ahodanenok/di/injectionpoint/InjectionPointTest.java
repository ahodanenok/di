package ahodanenok.di.injectionpoint;

import ahodanenok.di.DIContainer;
import ahodanenok.di.injectionpoint.classess.*;
import ahodanenok.di.interceptor.InjectionPoint;
import ahodanenok.di.value.InstantiatingValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InjectionPointTest {

    @Test
    public void testConstructorParameterInjectionPointInjectedAsConstructorParameter() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointConstructorParameter.class))
                .addValue(new InstantiatingValue<>(InjectingToConstructorClassWithConstructorInjectionPoint.class))
                .build();

        InjectingToConstructorClassWithConstructorInjectionPoint injectingToConstructor = container.instance(InjectingToConstructorClassWithConstructorInjectionPoint.class);
        assertNotNull(injectingToConstructor);

        InjectionPoint injectionPoint = injectingToConstructor.getObj().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToConstructorClassWithConstructorInjectionPoint.class.getDeclaredConstructor(ClassWithInjectionPointConstructorParameter.class),
                injectionPoint.getTarget());
        assertEquals(0, injectionPoint.getParameterIndex());
    }

    @Test
    public void testConstructorParameterInjectionPointInjectedAsField() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointField.class))
                .addValue(new InstantiatingValue<>(InjectingToConstructorClassWithFieldInjectionPoint.class))
                .build();

        InjectingToConstructorClassWithFieldInjectionPoint injectingToConstructor = container.instance(InjectingToConstructorClassWithFieldInjectionPoint.class);
        assertNotNull(injectingToConstructor);

        InjectionPoint injectionPoint = injectingToConstructor.getObj().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToConstructorClassWithFieldInjectionPoint.class.getDeclaredConstructor(ClassWithInjectionPointField.class),
                injectionPoint.getTarget());
    }


    @Test
    public void testConstructorParameterInjectionPointInjectedAsMethodParameter() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointMethod.class))
                .addValue(new InstantiatingValue<>(InjectingToConstructorClassWithMethodInjectionPoint.class))
                .build();

        InjectingToConstructorClassWithMethodInjectionPoint injectingToConstructor = container.instance(InjectingToConstructorClassWithMethodInjectionPoint.class);
        assertNotNull(injectingToConstructor);

        InjectionPoint injectionPoint = injectingToConstructor.getObj().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToConstructorClassWithMethodInjectionPoint.class.getDeclaredConstructor(ClassWithInjectionPointMethod.class),
                injectionPoint.getTarget());
    }

    @Test
    public void testFieldInjectionPointInjectedAsConstructorParameter() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointConstructorParameter.class))
                .addValue(new InstantiatingValue<>(InjectingToFieldClassWithConstructorInjectionPoint.class))
                .build();

        InjectingToFieldClassWithConstructorInjectionPoint injectingToField = container.instance(InjectingToFieldClassWithConstructorInjectionPoint.class);
        assertNotNull(injectingToField);

        InjectionPoint injectionPoint = injectingToField.getField().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToFieldClassWithConstructorInjectionPoint.class.getDeclaredField("field"),
                injectionPoint.getTarget());
    }

    @Test
    public void testFieldInjectionPointInjectedAsField() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointField.class))
                .addValue(new InstantiatingValue<>(InjectingToFieldClassWithFieldInjectionPoint.class))
                .build();

        InjectingToFieldClassWithFieldInjectionPoint injectingToField = container.instance(InjectingToFieldClassWithFieldInjectionPoint.class);
        assertNotNull(injectingToField);

        InjectionPoint injectionPoint = injectingToField.getField().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToFieldClassWithFieldInjectionPoint.class.getDeclaredField("field"),
                injectionPoint.getTarget());
    }

    @Test
    public void testFieldInjectionPointInjectedAsMethodParameter() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointMethod.class))
                .addValue(new InstantiatingValue<>(InjectingToFieldClassWithMethodInjectionPoint.class))
                .build();

        InjectingToFieldClassWithMethodInjectionPoint injectingToField = container.instance(InjectingToFieldClassWithMethodInjectionPoint.class);
        assertNotNull(injectingToField);

        InjectionPoint injectionPoint = injectingToField.getField().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToFieldClassWithMethodInjectionPoint.class.getDeclaredField("field"),
                injectionPoint.getTarget());
    }

    @Test
    public void testMethodInjectionPointInjectedAsConstructorParameter() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointConstructorParameter.class))
                .addValue(new InstantiatingValue<>(InjectingToMethodClassWithConstructorInjectionPoint.class))
                .build();

        InjectingToMethodClassWithConstructorInjectionPoint injectingToMethod = container.instance(InjectingToMethodClassWithConstructorInjectionPoint.class);
        assertNotNull(injectingToMethod);

        InjectionPoint injectionPoint = injectingToMethod.getObj().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToMethodClassWithConstructorInjectionPoint.class.getDeclaredMethod("setObj", ClassWithInjectionPointConstructorParameter.class),
                injectionPoint.getTarget());
    }

    @Test
    public void testMethodInjectionPointInjectedAsField() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointField.class))
                .addValue(new InstantiatingValue<>(InjectingToMethodClassWithFieldInjectionPoint.class))
                .build();

        InjectingToMethodClassWithFieldInjectionPoint injectingToMethod = container.instance(InjectingToMethodClassWithFieldInjectionPoint.class);
        assertNotNull(injectingToMethod);

        InjectionPoint injectionPoint = injectingToMethod.getObj().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToMethodClassWithFieldInjectionPoint.class.getDeclaredMethod("setObj", ClassWithInjectionPointField.class),
                injectionPoint.getTarget());
    }

    @Test
    public void testMethodInjectionPointInjectedAsMethodParameter() throws Exception {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(ClassWithInjectionPointMethod.class))
                .addValue(new InstantiatingValue<>(InjectingToMethodClassWithMethodInjectionPoint.class))
                .build();

        InjectingToMethodClassWithMethodInjectionPoint injectingToMethod = container.instance(InjectingToMethodClassWithMethodInjectionPoint.class);
        assertNotNull(injectingToMethod);

        InjectionPoint injectionPoint = injectingToMethod.getObj().getInjectionPoint();
        assertNotNull(injectionPoint);
        assertEquals(
                InjectingToMethodClassWithMethodInjectionPoint.class.getDeclaredMethod("setObj", ClassWithInjectionPointMethod.class),
                injectionPoint.getTarget());
    }
}
