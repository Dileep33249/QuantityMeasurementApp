import org.quantitymeasurement.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testLengthEquality_FeetAndInches() {
        Quantity<LengthUnit> oneFoot     = new Quantity<>(1.0,  LengthUnit.FEET);
        Quantity<LengthUnit> twelveInch  = new Quantity<>(12.0, LengthUnit.INCHES);
        assertEquals(oneFoot, twelveInch);
    }

    @Test
    void testLengthEquality_YardsAndInches() {
        Quantity<LengthUnit> oneYard    = new Quantity<>(1.0,  LengthUnit.YARDS);
        Quantity<LengthUnit> thirtySix  = new Quantity<>(36.0, LengthUnit.INCHES);
        assertEquals(oneYard, thirtySix);
    }

    @Test
    void testLengthInequality() {
        Quantity<LengthUnit> oneFoot = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> twoFeet = new Quantity<>(2.0, LengthUnit.FEET);
        assertNotEquals(oneFoot, twoFeet);
    }

    @Test
    void testWeightEquality_KilogramAndGram() {
        Quantity<WeightUnit> oneKg      = new Quantity<>(1.0,    WeightUnit.KILOGRAM);
        Quantity<WeightUnit> thousandGm = new Quantity<>(1000.0, WeightUnit.GRAM);
        assertEquals(oneKg, thousandGm);
    }

    @Test
    void testWeightInequality() {
        Quantity<WeightUnit> oneKg  = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> twoKg  = new Quantity<>(2.0, WeightUnit.KILOGRAM);
        assertNotEquals(oneKg, twoKg);
    }

    @Test
    void testLengthConversion_FeetToInches() {
        Quantity<LengthUnit> oneFoot  = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> result   = oneFoot.convertTo(LengthUnit.INCHES);
        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    void testLengthConversion_YardsToFeet() {
        Quantity<LengthUnit> oneYard = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> result  = oneYard.convertTo(LengthUnit.FEET);
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testLengthConversion_InchesToYards() {
        Quantity<LengthUnit> inches = new Quantity<>(36.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = inches.convertTo(LengthUnit.YARDS);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    void testWeightConversion_KilogramToGram() {
        Quantity<WeightUnit> oneKg = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> result = oneKg.convertTo(WeightUnit.GRAM);
        assertEquals(1000.0, result.getValue(), EPSILON);
    }

    @Test
    void testWeightConversion_GramToKilogram() {
        Quantity<WeightUnit> grams  = new Quantity<>(500.0, WeightUnit.GRAM);
        Quantity<WeightUnit> result = grams.convertTo(WeightUnit.KILOGRAM);
        assertEquals(0.5, result.getValue(), EPSILON);
    }

    @Test
    void testLengthAddition_SameUnit() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = a.add(b);
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testLengthAddition_DifferentUnits_TargetFeet() {
        Quantity<LengthUnit> oneFoot    = new Quantity<>(1.0,  LengthUnit.FEET);
        Quantity<LengthUnit> twelveInch = new Quantity<>(12.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result     = oneFoot.add(twelveInch, LengthUnit.FEET);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testWeightAddition_DifferentUnits_TargetKilogram() {
        Quantity<WeightUnit> oneKg    = new Quantity<>(1.0,    WeightUnit.KILOGRAM);
        Quantity<WeightUnit> thousandG = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> result   = oneKg.add(thousandG, WeightUnit.KILOGRAM);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testCrossCategoryPrevention_EqualityReturnsFalse() {
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertNotEquals(length, weight);
    }

    @Test
    void testConstructor_NullUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, (LengthUnit) null));
    }

    @Test
    void testConstructor_NaNValue_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testConstructor_PositiveInfinity_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.POSITIVE_INFINITY, WeightUnit.GRAM));
    }

    @Test
    void testConstructor_NegativeInfinity_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NEGATIVE_INFINITY, LengthUnit.YARDS));
    }

    @Test
    void testConvertTo_NullTarget_Throws() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q.convertTo(null));
    }

    @Test
    void testAdd_NullOther_Throws() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q.add(null));
    }

    @Test
    void testAdd_NullTargetUnit_Throws() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> a.add(b, null));
    }

    @Test
    void testConvertTo_ReturnsNewInstance() {
        Quantity<LengthUnit> original  = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> converted = original.convertTo(LengthUnit.INCHES);
        assertNotSame(original, converted);
    }

    @Test
    void testImmutability_AddReturnsNewInstance() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = a.add(b);
        assertNotSame(a, result);
        assertNotSame(b, result);
    }

    @Test
    void testEquals_SameInstance() {
        Quantity<LengthUnit> q = new Quantity<>(5.0, LengthUnit.FEET);
        assertEquals(q, q);
    }

    @Test
    void testEquals_NotEqualToNull() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        assertNotEquals(null, q);
    }

    @Test
    void testEquals_Symmetric() {
        Quantity<LengthUnit> a = new Quantity<>(1.0,  LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCHES);
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void testEquals_Transitive() {
        Quantity<LengthUnit> a = new Quantity<>(1.0,  LengthUnit.YARDS);
        Quantity<LengthUnit> b = new Quantity<>(3.0,  LengthUnit.FEET);
        Quantity<LengthUnit> c = new Quantity<>(36.0, LengthUnit.INCHES);
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    void testHashCode_EqualObjects_EqualHashCodes() {
        Quantity<LengthUnit> a = new Quantity<>(1.0,  LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCHES);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString_Format() {
        Quantity<LengthUnit> q = new Quantity<>(3.0, LengthUnit.YARDS);
        assertEquals("Quantity(3.0, YARDS)", q.toString());
    }

    @Test
    void testConversion_SameUnit_ReturnsSameValue() {
        Quantity<LengthUnit> q = new Quantity<>(5.0, LengthUnit.FEET);
        assertEquals(5.0, q.convertTo(LengthUnit.FEET).getValue(), EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        Quantity<WeightUnit> q = new Quantity<>(0.0, WeightUnit.KILOGRAM);
        assertEquals(0.0, q.convertTo(WeightUnit.GRAM).getValue(), EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        Quantity<LengthUnit> q = new Quantity<>(-1.0, LengthUnit.FEET);
        assertEquals(-12.0, q.convertTo(LengthUnit.INCHES).getValue(), EPSILON);
    }

    @Test
    void testRoundTrip_LengthConversion() {
        Quantity<LengthUnit> original = new Quantity<>(7.5, LengthUnit.FEET);
        Quantity<LengthUnit> toInches = original.convertTo(LengthUnit.INCHES);
        Quantity<LengthUnit> back     = toInches.convertTo(LengthUnit.FEET);
        assertEquals(original.getValue(), back.getValue(), EPSILON);
    }

    @Test
    void testRoundTrip_WeightConversion() {
        Quantity<WeightUnit> original = new Quantity<>(2.5, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> toPound  = original.convertTo(WeightUnit.POUND);
        Quantity<WeightUnit> back     = toPound.convertTo(WeightUnit.KILOGRAM);
        assertEquals(original.getValue(), back.getValue(), 0.01);
    }

    @Test
    void testIMeasurable_LengthUnit_ConvertToBase() {
        assertEquals(1.0,  LengthUnit.FEET.convertToBaseUnit(1.0),    EPSILON);
        assertEquals(1.0,  LengthUnit.INCHES.convertToBaseUnit(12.0), EPSILON);
        assertEquals(3.0,  LengthUnit.YARDS.convertToBaseUnit(1.0),   EPSILON);
    }

    @Test
    void testIMeasurable_WeightUnit_ConvertToBase() {
        assertEquals(1.0,    WeightUnit.KILOGRAM.convertToBaseUnit(1.0),    EPSILON);
        assertEquals(0.001,  WeightUnit.GRAM.convertToBaseUnit(1.0),        EPSILON);
        assertEquals(1000.0, WeightUnit.TONNE.convertToBaseUnit(1.0),       EPSILON);
    }

    @Test
    void testScalability_NewUnitEnum() {
        Quantity<WeightUnit> ounces = new Quantity<>(16.0, WeightUnit.OUNCE);
        Quantity<WeightUnit> result = ounces.convertTo(WeightUnit.GRAM);
        assertTrue(result.getValue() > 0);
    }
}