package org.quantitymeasurement;

import org.quantitymeasurement.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthTest {

    @Test
    void testEquality_FeetToFeet_SameValue() {
        assertEquals(
                new QuantityLength(1, LengthUnit.FEET),
                new QuantityLength(1, LengthUnit.FEET));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        assertEquals(
                new QuantityLength(1, LengthUnit.INCH),
                new QuantityLength(1, LengthUnit.INCH));
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        assertEquals(
                new QuantityLength(12, LengthUnit.INCH),
                new QuantityLength(1, LengthUnit.FEET));
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        assertNotEquals(
                new QuantityLength(1, LengthUnit.FEET),
                new QuantityLength(2, LengthUnit.FEET));
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        assertNotEquals(
                new QuantityLength(1, LengthUnit.INCH),
                new QuantityLength(2, LengthUnit.INCH));
    }

    @Test
    void testEquality_SameReference() {
        QuantityLength q = new QuantityLength(1, LengthUnit.FEET);
        assertEquals(q, q);
    }

    @Test
    void testEquality_NullComparison() {
        QuantityLength q = new QuantityLength(1, LengthUnit.FEET);
        assertNotEquals(q, null);
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new QuantityLength(1, null));
    }

    @Test
    void testEquality_InvalidValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new QuantityLength(Double.NaN,
                        LengthUnit.FEET));
    }
}
