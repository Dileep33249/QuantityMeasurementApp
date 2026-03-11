import org.quantitymeasurement.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testConversion_FeetToInches() {
        double result = QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(12.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        double result = QuantityLength.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        double result = QuantityLength.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
        assertEquals(36.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        double result = QuantityLength.convert(72.0, LengthUnit.INCHES, LengthUnit.YARDS);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_FeetToYards() {
        double result = QuantityLength.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_YardsToFeet() {
        double result = QuantityLength.convert(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        assertEquals(9.0, result, EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        double result = QuantityLength.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToCentimeters() {
        double result = QuantityLength.convert(1.0, LengthUnit.INCHES, LengthUnit.CENTIMETERS);
        assertEquals(2.54, result, EPSILON);
    }

    @Test
    void testConversion_CentimetersToFeet() {
        double result = QuantityLength.convert(30.48, LengthUnit.CENTIMETERS, LengthUnit.FEET);
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        double result = QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(0.0, result, EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        double result = QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(-12.0, result, EPSILON);
    }

    @Test
    void testConversion_SameUnit() {
        double result = QuantityLength.convert(5.0, LengthUnit.FEET, LengthUnit.FEET);
        assertEquals(5.0, result, EPSILON);
    }

    @Test
    void testConversion_RoundTrip_FeetInches() {
        double original   = 7.5;
        double toInches   = QuantityLength.convert(original, LengthUnit.FEET, LengthUnit.INCHES);
        double backToFeet = QuantityLength.convert(toInches, LengthUnit.INCHES, LengthUnit.FEET);
        assertEquals(original, backToFeet, EPSILON);
    }

    @Test
    void testConversion_RoundTrip_YardsCm() {
        double original    = 2.0;
        double toCm        = QuantityLength.convert(original, LengthUnit.YARDS, LengthUnit.CENTIMETERS);
        double backToYards = QuantityLength.convert(toCm, LengthUnit.CENTIMETERS, LengthUnit.YARDS);
        assertEquals(original, backToYards, EPSILON);
    }

    @Test
    void testConversion_MultiStep_RoundTrip() {
        double v  = 5.0;
        double ab = QuantityLength.convert(v,  LengthUnit.FEET,        LengthUnit.CENTIMETERS);
        double bc = QuantityLength.convert(ab, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        double ca = QuantityLength.convert(bc, LengthUnit.INCHES,      LengthUnit.FEET);
        assertEquals(v, ca, EPSILON);
    }

    @Test
    void testEquality_OneYard_ThreeFeet() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        assertEquals(yard, feet);
    }

    @Test
    void testEquality_ThirtySixInches_OneYard() {
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength yard   = new QuantityLength(1.0,  LengthUnit.YARDS);
        assertEquals(inches, yard);
    }

    @Test
    void testEquality_TwelveInches_OneFoot() {
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength foot   = new QuantityLength(1.0,  LengthUnit.FEET);
        assertEquals(inches, foot);
    }

    @Test
    void testInequality_DifferentLengths() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.FEET);
        assertNotEquals(a, b);
    }

    @Test
    void testEquality_SameInstance() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        assertEquals(a, a);
    }

    @Test
    void testEquality_NotEqualToNull() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        assertNotEquals(null, a);
    }

    @Test
    void testConvertTo_ReturnsNewInstance() {
        QuantityLength original  = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength converted = original.convertTo(LengthUnit.INCHES);
        assertNotSame(original, converted);
        assertEquals(LengthUnit.INCHES, converted.getUnit());
        assertEquals(12.0, converted.getValue(), EPSILON);
    }

    @Test
    void testConvertTo_SameUnit_ReturnsSameValue() {
        QuantityLength q = new QuantityLength(7.0, LengthUnit.YARDS);
        assertEquals(7.0, q.convertTo(LengthUnit.YARDS).getValue(), EPSILON);
    }

    @ParameterizedTest(name = "convert({0} FEET → INCHES) = {1}")
    @CsvSource({
            "1.0,  12.0",
            "2.0,  24.0",
            "0.5,   6.0",
            "3.0,  36.0"
    })
    void testConversion_FeetToInches_Parameterized(double feet, double expectedInches) {
        double result = QuantityLength.convert(feet, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(expectedInches, result, EPSILON);
    }

    @Test
    void testConversion_NullSourceUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(1.0, null, LengthUnit.INCHES));
    }

    @Test
    void testConversion_NullTargetUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(1.0, LengthUnit.FEET, null));
    }

    @Test
    void testConversion_NaNValue_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES));
    }

    @Test
    void testConversion_PositiveInfinity_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES));
    }

    @Test
    void testConversion_NegativeInfinity_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(Double.NEGATIVE_INFINITY, LengthUnit.YARDS, LengthUnit.FEET));
    }

    @Test
    void testConstructor_NullUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testConstructor_NaNValue_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testConvertTo_NullUnit_Throws() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q.convertTo(null));
    }

    @Test
    void testToString_Format() {
        QuantityLength q = new QuantityLength(3.0, LengthUnit.YARDS);
        assertEquals("3.0 YARDS", q.toString());
    }
}