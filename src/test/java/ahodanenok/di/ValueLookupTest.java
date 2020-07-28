package ahodanenok.di;

import org.junit.jupiter.api.Test;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
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
    public void lookupExact_1() {
        DependencyValueExactLookup lookup = new DependencyValueExactLookup();

        Set<DependencyValue<?>> values = new LinkedHashSet<>();
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class)), "1"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(B.class)), "2"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(C.class)), "3"));

        Set<DependencyValue<String>> value = lookup.execute(values, DependencyIdentifier.of(String.class));
        assertEquals(0, value.size());
    }

    @Test
    public void lookupExact_2() {
        DependencyValueExactLookup lookup = new DependencyValueExactLookup();

        Set<DependencyValue<?>> values = new LinkedHashSet<>();
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class)), "1"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(B.class)), "2"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(C.class)), "3"));

        Set<DependencyValue<String>> value = lookup.execute(values, DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class)));
        assertEquals(1, value.size());
        assertEquals("1", value.iterator().next().provider().get());
    }

    @Test
    public void lookupExact_3() {
        DependencyValueExactLookup lookup = new DependencyValueExactLookup();

        Set<DependencyValue<?>> values = new LinkedHashSet<>();
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class)), "1"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(B.class)), "2"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(C.class)), "3"));

        Set<DependencyValue<String>> value = lookup.execute(values,
                DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class), T.class.getAnnotation(B.class)));
        assertEquals(0, value.size());
    }

    @Test
    public void lookupExact_4() {
        DependencyValueExactLookup lookup = new DependencyValueExactLookup();

        Set<DependencyValue<?>> values = new LinkedHashSet<>();
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class), T.class.getAnnotation(C.class)), "1"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(B.class)), "2"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(C.class)), "3"));

        Set<DependencyValue<String>> value = lookup.execute(values, DependencyIdentifier.of(String.class, T.class.getAnnotation(C.class)));
        assertEquals(2, value.size());
        assertTrue(value.stream().map(v -> v.provider().get()).collect(Collectors.toList()).containsAll(Arrays.asList("1", "3")));
    }

    @Test
    public void lookupExact_5() {
        DependencyValueExactLookup lookup = new DependencyValueExactLookup();

        Set<DependencyValue<?>> values = new LinkedHashSet<>();
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class), T.class.getAnnotation(B.class)), "1"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(B.class)), "2"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(C.class)), "3"));

        Set<DependencyValue<String>> value = lookup.execute(values, DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class), T.class.getAnnotation(B.class)));
        assertEquals(1, value.size());
        assertEquals("1", value.iterator().next().provider().get());
    }

    @Test
    public void lookupExact_6() {
        DependencyValueExactLookup lookup = new DependencyValueExactLookup();

        Set<DependencyValue<?>> values = new LinkedHashSet<>();
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class)), "1"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(A.class), T.class.getAnnotation(C.class), T.class.getAnnotation(B.class)), "2"));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(String.class, T.class.getAnnotation(C.class)), "3"));

        Set<DependencyValue<String>> value = lookup.execute(values, DependencyIdentifier.of(String.class, T.class.getAnnotation(B.class), T.class.getAnnotation(C.class)));
        assertEquals(1, value.size());
        assertEquals("2", value.iterator().next().provider().get());
    }
}
