package ahodanenok.di;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PrimitivesLookupTest {

    @Test
    public void testPrimitives_1() {
        Set<DependencyValue<?>> values = new HashSet<>();
        values.add(new DependencyInstanceValue<>((byte) 1));
        values.add(new DependencyInstanceValue<>((short) 2));
        values.add(new DependencyInstanceValue<>(3));
        values.add(new DependencyInstanceValue<>((long) 4));
        values.add(new DependencyInstanceValue<>((float) 5));
        values.add(new DependencyInstanceValue<>(6.0));
        values.add(new DependencyInstanceValue<>(true));
        values.add(new DependencyInstanceValue<>('z'));

        DependencyValueExactLookup lookup = new DependencyValueExactLookup();

        // byte
        Set<DependencyValue<Byte>> byteResult = lookup.execute(values, DependencyIdentifier.of(byte.class));
        assertEquals(1, byteResult.size());
        assertEquals((byte) 1, byteResult.iterator().next().provider().get());

        byteResult = lookup.execute(values, DependencyIdentifier.of(Byte.class));
        assertEquals(1, byteResult.size());
        assertEquals((byte) 1, byteResult.iterator().next().provider().get());

        // short
        Set<DependencyValue<Short>> shortResult = lookup.execute(values, DependencyIdentifier.of(short.class));
        assertEquals(1, shortResult.size());
        assertEquals((short) 2, shortResult.iterator().next().provider().get());

        shortResult = lookup.execute(values, DependencyIdentifier.of(Short.class));
        assertEquals(1, shortResult.size());
        assertEquals((short) 2, shortResult.iterator().next().provider().get());

        // int
        Set<DependencyValue<Integer>> intResult = lookup.execute(values, DependencyIdentifier.of(int.class));
        assertEquals(1, intResult.size());
        assertEquals((int) 3, intResult.iterator().next().provider().get());

        intResult = lookup.execute(values, DependencyIdentifier.of(Integer.class));
        assertEquals(1, intResult.size());
        assertEquals(3, intResult.iterator().next().provider().get());

        // long
        Set<DependencyValue<Long>> longResult = lookup.execute(values, DependencyIdentifier.of(long.class));
        assertEquals(1, longResult.size());
        assertEquals(4, longResult.iterator().next().provider().get());

        longResult = lookup.execute(values, DependencyIdentifier.of(Long.class));
        assertEquals(1, longResult.size());
        assertEquals(4, longResult.iterator().next().provider().get());

        // float
        Set<DependencyValue<Float>> floatResult = lookup.execute(values, DependencyIdentifier.of(float.class));
        assertEquals(1, floatResult.size());
        assertEquals((float) 5, floatResult.iterator().next().provider().get());

        floatResult = lookup.execute(values, DependencyIdentifier.of(Float.class));
        assertEquals(1, floatResult.size());
        assertEquals((float) 5, floatResult.iterator().next().provider().get());

        // double
        Set<DependencyValue<Double>> doubleResult = lookup.execute(values, DependencyIdentifier.of(double.class));
        assertEquals(1, doubleResult.size());
        assertEquals((double) 6, doubleResult.iterator().next().provider().get());

        doubleResult = lookup.execute(values, DependencyIdentifier.of(Double.class));
        assertEquals(1, doubleResult.size());
        assertEquals((double) 6, doubleResult.iterator().next().provider().get());

        // boolean
        Set<DependencyValue<Boolean>> booleanResult = lookup.execute(values, DependencyIdentifier.of(boolean.class));
        assertEquals(1, booleanResult.size());
        assertEquals(true, booleanResult.iterator().next().provider().get());

        booleanResult = lookup.execute(values, DependencyIdentifier.of(Boolean.class));
        assertEquals(1, booleanResult.size());
        assertEquals(true, booleanResult.iterator().next().provider().get());

        // char
        Set<DependencyValue<Character>> characterResult = lookup.execute(values, DependencyIdentifier.of(char.class));
        assertEquals(1, characterResult.size());
        assertEquals('z', characterResult.iterator().next().provider().get());

        characterResult = lookup.execute(values, DependencyIdentifier.of(Character.class));
        assertEquals(1, characterResult.size());
        assertEquals('z', characterResult.iterator().next().provider().get());
    }

    @Test
    public void testPrimitives_2() {
        Set<DependencyValue<?>> values = new HashSet<>();
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(byte.class), (byte) 1));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(short.class), (short) 2));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(int.class), 3));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(long.class), (long) 4));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(float.class), (float) 5));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(double.class), 6.0));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(boolean.class), true));
        values.add(new DependencyInstanceValue<>(DependencyIdentifier.of(char.class), 'z'));

        DependencyValueExactLookup lookup = new DependencyValueExactLookup();

        // byte
        Set<DependencyValue<Byte>> byteResult = lookup.execute(values, DependencyIdentifier.of(byte.class));
        assertEquals(1, byteResult.size());
        assertEquals((byte) 1, byteResult.iterator().next().provider().get());

        byteResult = lookup.execute(values, DependencyIdentifier.of(Byte.class));
        assertEquals(1, byteResult.size());
        assertEquals((byte) 1, byteResult.iterator().next().provider().get());

        // short
        Set<DependencyValue<Short>> shortResult = lookup.execute(values, DependencyIdentifier.of(short.class));
        assertEquals(1, shortResult.size());
        assertEquals((short) 2, shortResult.iterator().next().provider().get());

        shortResult = lookup.execute(values, DependencyIdentifier.of(Short.class));
        assertEquals(1, shortResult.size());
        assertEquals((short) 2, shortResult.iterator().next().provider().get());

        // int
        Set<DependencyValue<Integer>> intResult = lookup.execute(values, DependencyIdentifier.of(int.class));
        assertEquals(1, intResult.size());
        assertEquals((int) 3, intResult.iterator().next().provider().get());

        intResult = lookup.execute(values, DependencyIdentifier.of(Integer.class));
        assertEquals(1, intResult.size());
        assertEquals(3, intResult.iterator().next().provider().get());

        // long
        Set<DependencyValue<Long>> longResult = lookup.execute(values, DependencyIdentifier.of(long.class));
        assertEquals(1, longResult.size());
        assertEquals(4, longResult.iterator().next().provider().get());

        longResult = lookup.execute(values, DependencyIdentifier.of(Long.class));
        assertEquals(1, longResult.size());
        assertEquals(4, longResult.iterator().next().provider().get());

        // float
        Set<DependencyValue<Float>> floatResult = lookup.execute(values, DependencyIdentifier.of(float.class));
        assertEquals(1, floatResult.size());
        assertEquals((float) 5, floatResult.iterator().next().provider().get());

        floatResult = lookup.execute(values, DependencyIdentifier.of(Float.class));
        assertEquals(1, floatResult.size());
        assertEquals((float) 5, floatResult.iterator().next().provider().get());

        // double
        Set<DependencyValue<Double>> doubleResult = lookup.execute(values, DependencyIdentifier.of(double.class));
        assertEquals(1, doubleResult.size());
        assertEquals((double) 6, doubleResult.iterator().next().provider().get());

        doubleResult = lookup.execute(values, DependencyIdentifier.of(Double.class));
        assertEquals(1, doubleResult.size());
        assertEquals((double) 6, doubleResult.iterator().next().provider().get());

        // boolean
        Set<DependencyValue<Boolean>> booleanResult = lookup.execute(values, DependencyIdentifier.of(boolean.class));
        assertEquals(1, booleanResult.size());
        assertEquals(true, booleanResult.iterator().next().provider().get());

        booleanResult = lookup.execute(values, DependencyIdentifier.of(Boolean.class));
        assertEquals(1, booleanResult.size());
        assertEquals(true, booleanResult.iterator().next().provider().get());

        // char
        Set<DependencyValue<Character>> characterResult = lookup.execute(values, DependencyIdentifier.of(char.class));
        assertEquals(1, characterResult.size());
        assertEquals('z', characterResult.iterator().next().provider().get());

        characterResult = lookup.execute(values, DependencyIdentifier.of(Character.class));
        assertEquals(1, characterResult.size());
        assertEquals('z', characterResult.iterator().next().provider().get());
    }
}
