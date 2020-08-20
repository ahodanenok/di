package ahodanenok.di.valuelookup;

import ahodanenok.di.Any;
import ahodanenok.di.ValueSpecifier;
import ahodanenok.di.ValueExactLookup;
import ahodanenok.di.value.InstanceValue;
import ahodanenok.di.value.Value;
import ahodanenok.di.valuelookup.classes.QualifiedClass;
import ahodanenok.di.valuelookup.classes.QualifierA;
import ahodanenok.di.valuelookup.classes.QualifierB;
import ahodanenok.di.valuelookup.classes.QualifierC;
import org.junit.jupiter.api.Test;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ValueLookupTest {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface A { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface B { }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface C { }

    @A @B @C
    class T { }

    @Test
    public void testExactLookupByClassAllFound() {
        ValueExactLookup lookup = new ValueExactLookup();

        Set<Value<?>> values = new LinkedHashSet<>();
        values.add(new InstanceValue<>("1"));
        values.add(new InstanceValue<>("2"));
        values.add(new InstanceValue<>(new QualifiedClass()));
        values.add(new InstanceValue<>(CharSequence.class, "3"));

        Set<Value<String>> value = lookup.execute(values, ValueSpecifier.of(String.class));
        assertEquals(2, value.size());

        Set<String> strings = value.stream().map(v -> v.provider().get()).collect(Collectors.toSet());
        assertTrue(strings.contains("1"));
        assertTrue(strings.contains("2"));
    }

    @Test
    public void testExactLookupByClassNoneFound() {
        ValueExactLookup lookup = new ValueExactLookup();

        Set<Value<?>> values = new LinkedHashSet<>();
        values.add(new InstanceValue<>(1));
        values.add(new InstanceValue<>(2));
        values.add(new InstanceValue<>(new QualifiedClass()));
        values.add(new InstanceValue<>(Number.class, 3));

        Set<Value<Long>> value = lookup.execute(values, ValueSpecifier.of(Long.class));
        assertEquals(0, value.size());
    }

    @Test
    public void testExactLookupByClassAndQualifiersAllFound() {
        ValueExactLookup lookup = new ValueExactLookup();

        Set<Value<?>> values = new LinkedHashSet<>();

        InstanceValue<String> v1 = new InstanceValue<>("1");
        v1.metadata().setQualifiers(QualifiedClass.class.getAnnotation(QualifierA.class));
        values.add(v1);

        InstanceValue<String> v2 = new InstanceValue<>("2");
        values.add(v2);

        InstanceValue<String> v3 = new InstanceValue<>("3");
        v3.metadata().setQualifiers(
                QualifiedClass.class.getAnnotation(QualifierA.class),
                QualifiedClass.class.getAnnotation(QualifierB.class));
        values.add(v3);

        values.add(new InstanceValue<>(new QualifiedClass()));
        values.add(new InstanceValue<>(CharSequence.class, "4"));

        Set<Value<String>> value = lookup.execute(values, ValueSpecifier.of(String.class, QualifiedClass.class.getAnnotation(QualifierA.class)));
        assertEquals(2, value.size());

        Set<String> strings = value.stream().map(v -> v.provider().get()).collect(Collectors.toSet());
        assertTrue(strings.contains("1"));
        assertTrue(strings.contains("3"));
    }

    @Test
    public void testExactLookupByClassAndQualifiersNoneFound() {
        ValueExactLookup lookup = new ValueExactLookup();

        Set<Value<?>> values = new LinkedHashSet<>();

        InstanceValue<String> v1 = new InstanceValue<>("1");
        v1.metadata().setQualifiers(QualifiedClass.class.getAnnotation(QualifierC.class));
        values.add(v1);

        InstanceValue<String> v2 = new InstanceValue<>("2");
        values.add(v2);

        InstanceValue<String> v3 = new InstanceValue<>("3");
        v3.metadata().setQualifiers(
                QualifiedClass.class.getAnnotation(QualifierB.class),
                QualifiedClass.class.getAnnotation(QualifierC.class));
        values.add(v3);

        values.add(new InstanceValue<>(new QualifiedClass()));
        values.add(new InstanceValue<>(CharSequence.class, "4"));

        Set<Value<String>> value = lookup.execute(
                values,
                ValueSpecifier.of(
                        String.class,
                        QualifiedClass.class.getAnnotation(QualifierA.class),
                        QualifiedClass.class.getAnnotation(QualifierC.class)));
        assertEquals(0, value.size());
    }

    @Test
    public void testExactLookupByAnyQualifiers() {
        ValueExactLookup lookup = new ValueExactLookup();

        Set<Value<?>> values = new LinkedHashSet<>();

        InstanceValue<String> v1 = new InstanceValue<>("1");
        v1.metadata().setQualifiers(QualifiedClass.class.getAnnotation(QualifierC.class));
        values.add(v1);

        InstanceValue<String> v2 = new InstanceValue<>("2");
        values.add(v2);

        InstanceValue<String> v3 = new InstanceValue<>("3");
        v3.metadata().setQualifiers(
                QualifiedClass.class.getAnnotation(QualifierB.class),
                QualifiedClass.class.getAnnotation(QualifierC.class));
        values.add(v3);

        Set<Value<String>> value = lookup.execute(
                values,
                ValueSpecifier.of(String.class, QualifiedClass.class.getAnnotation(Any.class)));
        assertEquals(3, value.size());

        Set<String> strings = value.stream().map(v -> v.provider().get()).collect(Collectors.toSet());
        assertTrue(strings.contains("1"));
        assertTrue(strings.contains("2"));
        assertTrue(strings.contains("3"));
    }

    @Test
    public void testExactLookupByQualifiers() {
        ValueExactLookup lookup = new ValueExactLookup();

        Set<Value<?>> values = new LinkedHashSet<>();

        InstanceValue<String> v1 = new InstanceValue<>("1");
        v1.metadata().setQualifiers(QualifiedClass.class.getAnnotation(QualifierA.class));
        values.add(v1);

        InstanceValue<String> v2 = new InstanceValue<>("2");
        values.add(v2);

        InstanceValue<Integer> v3 = new InstanceValue<>(3);
        v3.metadata().setQualifiers(
                QualifiedClass.class.getAnnotation(QualifierB.class),
                QualifiedClass.class.getAnnotation(QualifierA.class));
        values.add(v3);

        ValueSpecifier<?> s = new ValueSpecifier<>();
        s.setQualifiers(QualifiedClass.class.getAnnotation(QualifierA.class));

        Set<? extends Value<?>> value = lookup.execute(values, s);
        assertEquals(2, value.size());

        Set<?> strings = value.stream().map(v -> v.provider().get()).collect(Collectors.toSet());
        assertTrue(strings.contains("1"));
        assertTrue(strings.contains(3));
    }

    @Test
    public void testExactLookupByNameAndType() {
        ValueExactLookup lookup = new ValueExactLookup();

        Set<Value<?>> values = new LinkedHashSet<>();

        InstanceValue<String> v1 = new InstanceValue<>("1");
        v1.metadata().setQualifiers(QualifiedClass.class.getAnnotation(QualifierA.class));
        v1.metadata().setName("test");
        values.add(v1);

        InstanceValue<String> v2 = new InstanceValue<>("2");
        values.add(v2);

        InstanceValue<Integer> v3 = new InstanceValue<>(3);
        v3.metadata().setQualifiers(QualifiedClass.class.getAnnotation(QualifierA.class));
        v3.metadata().setName("test");
        values.add(v3);

        ValueSpecifier<String> s = new ValueSpecifier<>();
        s.setName("test");
        s.setType(String.class);

        Set<? extends Value<?>> value = lookup.execute(values, s);
        assertEquals(1, value.size());

        Set<?> strings = value.stream().map(v -> v.provider().get()).collect(Collectors.toSet());
        assertTrue(strings.contains("1"));
    }

    @Test
    public void testExactLookupByNameAndTypeAndQualifiers() {
        ValueExactLookup lookup = new ValueExactLookup();

        Set<Value<?>> values = new LinkedHashSet<>();

        InstanceValue<String> v1 = new InstanceValue<>("1");
        v1.metadata().setQualifiers(QualifiedClass.class.getAnnotation(QualifierA.class));
        v1.metadata().setName("test");
        values.add(v1);

        InstanceValue<String> v2 = new InstanceValue<>("2");
        values.add(v2);

        InstanceValue<Integer> v3 = new InstanceValue<>(3);
        v3.metadata().setQualifiers(QualifiedClass.class.getAnnotation(QualifierB.class));
        v3.metadata().setName("test");
        values.add(v3);

        ValueSpecifier<Integer> s = new ValueSpecifier<>();
        s.setName("test");
        s.setType(int.class);
        s.setQualifiers(QualifiedClass.class.getAnnotation(QualifierB.class));

        Set<? extends Value<?>> value = lookup.execute(values, s);
        assertEquals(1, value.size());

        Set<?> strings = value.stream().map(v -> v.provider().get()).collect(Collectors.toSet());
        assertTrue(strings.contains(3));
    }
}
