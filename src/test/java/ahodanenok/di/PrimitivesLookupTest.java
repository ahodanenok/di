package ahodanenok.di;

import ahodanenok.di.value.InstanceValue;
import ahodanenok.di.value.Value;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PrimitivesLookupTest {

    @Test
    public void testPrimitives_1() {
        Set<Value<?>> values = new HashSet<>();
        values.add(new InstanceValue<>((byte) 1));
        values.add(new InstanceValue<>((short) 2));
        values.add(new InstanceValue<>(3));
        values.add(new InstanceValue<>((long) 4));
        values.add(new InstanceValue<>((float) 5));
        values.add(new InstanceValue<>(6.0));
        values.add(new InstanceValue<>(true));
        values.add(new InstanceValue<>('z'));

        ValueExactLookup lookup = new ValueExactLookup();

        // byte
        Set<Value<Byte>> byteResult = lookup.execute(values, DependencyIdentifier.of(byte.class));
        assertEquals(1, byteResult.size());
        assertEquals((byte) 1, byteResult.iterator().next().provider().get());

        byteResult = lookup.execute(values, DependencyIdentifier.of(Byte.class));
        assertEquals(1, byteResult.size());
        assertEquals((byte) 1, byteResult.iterator().next().provider().get());

        // short
        Set<Value<Short>> shortResult = lookup.execute(values, DependencyIdentifier.of(short.class));
        assertEquals(1, shortResult.size());
        assertEquals((short) 2, shortResult.iterator().next().provider().get());

        shortResult = lookup.execute(values, DependencyIdentifier.of(Short.class));
        assertEquals(1, shortResult.size());
        assertEquals((short) 2, shortResult.iterator().next().provider().get());

        // int
        Set<Value<Integer>> intResult = lookup.execute(values, DependencyIdentifier.of(int.class));
        assertEquals(1, intResult.size());
        assertEquals((int) 3, intResult.iterator().next().provider().get());

        intResult = lookup.execute(values, DependencyIdentifier.of(Integer.class));
        assertEquals(1, intResult.size());
        assertEquals(3, intResult.iterator().next().provider().get());

        // long
        Set<Value<Long>> longResult = lookup.execute(values, DependencyIdentifier.of(long.class));
        assertEquals(1, longResult.size());
        assertEquals(4, longResult.iterator().next().provider().get());

        longResult = lookup.execute(values, DependencyIdentifier.of(Long.class));
        assertEquals(1, longResult.size());
        assertEquals(4, longResult.iterator().next().provider().get());

        // float
        Set<Value<Float>> floatResult = lookup.execute(values, DependencyIdentifier.of(float.class));
        assertEquals(1, floatResult.size());
        assertEquals((float) 5, floatResult.iterator().next().provider().get());

        floatResult = lookup.execute(values, DependencyIdentifier.of(Float.class));
        assertEquals(1, floatResult.size());
        assertEquals((float) 5, floatResult.iterator().next().provider().get());

        // double
        Set<Value<Double>> doubleResult = lookup.execute(values, DependencyIdentifier.of(double.class));
        assertEquals(1, doubleResult.size());
        assertEquals((double) 6, doubleResult.iterator().next().provider().get());

        doubleResult = lookup.execute(values, DependencyIdentifier.of(Double.class));
        assertEquals(1, doubleResult.size());
        assertEquals((double) 6, doubleResult.iterator().next().provider().get());

        // boolean
        Set<Value<Boolean>> booleanResult = lookup.execute(values, DependencyIdentifier.of(boolean.class));
        assertEquals(1, booleanResult.size());
        assertEquals(true, booleanResult.iterator().next().provider().get());

        booleanResult = lookup.execute(values, DependencyIdentifier.of(Boolean.class));
        assertEquals(1, booleanResult.size());
        assertEquals(true, booleanResult.iterator().next().provider().get());

        // char
        Set<Value<Character>> characterResult = lookup.execute(values, DependencyIdentifier.of(char.class));
        assertEquals(1, characterResult.size());
        assertEquals('z', characterResult.iterator().next().provider().get());

        characterResult = lookup.execute(values, DependencyIdentifier.of(Character.class));
        assertEquals(1, characterResult.size());
        assertEquals('z', characterResult.iterator().next().provider().get());
    }

    @Test
    public void testPrimitives_2() {
        Set<Value<?>> values = new HashSet<>();
        values.add(new InstanceValue<>(byte.class, (byte) 1));
        values.add(new InstanceValue<>(short.class, (short) 2));
        values.add(new InstanceValue<>(int.class, 3));
        values.add(new InstanceValue<>(long.class, (long) 4));
        values.add(new InstanceValue<>(float.class, (float) 5));
        values.add(new InstanceValue<>(double.class, 6.0));
        values.add(new InstanceValue<>(boolean.class, true));
        values.add(new InstanceValue<>(char.class, 'z'));

        ValueExactLookup lookup = new ValueExactLookup();

        // byte
        Set<Value<Byte>> byteResult = lookup.execute(values, DependencyIdentifier.of(byte.class));
        assertEquals(1, byteResult.size());
        assertEquals((byte) 1, byteResult.iterator().next().provider().get());

        byteResult = lookup.execute(values, DependencyIdentifier.of(Byte.class));
        assertEquals(1, byteResult.size());
        assertEquals((byte) 1, byteResult.iterator().next().provider().get());

        // short
        Set<Value<Short>> shortResult = lookup.execute(values, DependencyIdentifier.of(short.class));
        assertEquals(1, shortResult.size());
        assertEquals((short) 2, shortResult.iterator().next().provider().get());

        shortResult = lookup.execute(values, DependencyIdentifier.of(Short.class));
        assertEquals(1, shortResult.size());
        assertEquals((short) 2, shortResult.iterator().next().provider().get());

        // int
        Set<Value<Integer>> intResult = lookup.execute(values, DependencyIdentifier.of(int.class));
        assertEquals(1, intResult.size());
        assertEquals((int) 3, intResult.iterator().next().provider().get());

        intResult = lookup.execute(values, DependencyIdentifier.of(Integer.class));
        assertEquals(1, intResult.size());
        assertEquals(3, intResult.iterator().next().provider().get());

        // long
        Set<Value<Long>> longResult = lookup.execute(values, DependencyIdentifier.of(long.class));
        assertEquals(1, longResult.size());
        assertEquals(4, longResult.iterator().next().provider().get());

        longResult = lookup.execute(values, DependencyIdentifier.of(Long.class));
        assertEquals(1, longResult.size());
        assertEquals(4, longResult.iterator().next().provider().get());

        // float
        Set<Value<Float>> floatResult = lookup.execute(values, DependencyIdentifier.of(float.class));
        assertEquals(1, floatResult.size());
        assertEquals((float) 5, floatResult.iterator().next().provider().get());

        floatResult = lookup.execute(values, DependencyIdentifier.of(Float.class));
        assertEquals(1, floatResult.size());
        assertEquals((float) 5, floatResult.iterator().next().provider().get());

        // double
        Set<Value<Double>> doubleResult = lookup.execute(values, DependencyIdentifier.of(double.class));
        assertEquals(1, doubleResult.size());
        assertEquals((double) 6, doubleResult.iterator().next().provider().get());

        doubleResult = lookup.execute(values, DependencyIdentifier.of(Double.class));
        assertEquals(1, doubleResult.size());
        assertEquals((double) 6, doubleResult.iterator().next().provider().get());

        // boolean
        Set<Value<Boolean>> booleanResult = lookup.execute(values, DependencyIdentifier.of(boolean.class));
        assertEquals(1, booleanResult.size());
        assertEquals(true, booleanResult.iterator().next().provider().get());

        booleanResult = lookup.execute(values, DependencyIdentifier.of(Boolean.class));
        assertEquals(1, booleanResult.size());
        assertEquals(true, booleanResult.iterator().next().provider().get());

        // char
        Set<Value<Character>> characterResult = lookup.execute(values, DependencyIdentifier.of(char.class));
        assertEquals(1, characterResult.size());
        assertEquals('z', characterResult.iterator().next().provider().get());

        characterResult = lookup.execute(values, DependencyIdentifier.of(Character.class));
        assertEquals(1, characterResult.size());
        assertEquals('z', characterResult.iterator().next().provider().get());
    }
}
