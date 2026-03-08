import org.junit.jupiter.api.Test;
import org.quantitymeasurement.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityLengthTest {

    private static final double EPSILON = 1e-2;

    @Test
    public void testLengthUnitEnum_FeetConstant() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), EPSILON);
    }

    @Test
    public void testLengthUnitEnum_InchesConstant() {
        assertEquals(1.0 / 12.0, LengthUnit.INCHES.getConversionFactor(), EPSILON);
    }

    @Test
    public void testLengthUnitEnum_YardsConstant() {
        assertEquals(3.0, LengthUnit.YARDS.getConversionFactor(), EPSILON);
    }

    @Test
    public void testLengthUnitEnum_CentimetersConstant() {
        assertEquals(1.0 / 30.48, LengthUnit.CENTIMETERS.getConversionFactor(), EPSILON);
    }

    @Test
    public void testConvertToBaseUnit_FeetToFeet() {
        assertEquals(5.0, LengthUnit.FEET.convertToBaseUnit(5.0), EPSILON);
    }

    @Test
    public void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0, LengthUnit.INCHES.convertToBaseUnit(12.0), EPSILON);
    }

    @Test
    public void testConvertToBaseUnit_YardsToFeet() {
        assertEquals(3.0, LengthUnit.YARDS.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    public void testConvertToBaseUnit_CentimetersToFeet() {
        assertEquals(1.0, LengthUnit.CENTIMETERS.convertToBaseUnit(30.48), EPSILON);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToFeet() {
        assertEquals(2.0, LengthUnit.FEET.convertFromBaseUnit(2.0), EPSILON);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToYards() {
        assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(3.0), EPSILON);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToCentimeters() {
        assertEquals(30.48, LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    public void testQuantityLengthRefactored_Equality() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        assertTrue(a.equals(b));
    }

    @Test
    public void testQuantityLengthRefactored_ConvertTo() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength result = a.convertTo(LengthUnit.INCHES);
        assertEquals(LengthUnit.INCHES, result.getUnit());
        assertEquals(12.0, result.getValue(), EPSILON);
    }

    @Test
    public void testQuantityLengthRefactored_Add() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(a, b, LengthUnit.FEET);
        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    public void testQuantityLengthRefactored_AddWithTargetUnit() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(a, b, LengthUnit.YARDS);
        assertEquals(LengthUnit.YARDS, result.getUnit());
        assertEquals(0.67, result.getValue(), EPSILON);
    }

    @Test
    public void testQuantityLengthRefactored_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, null));
    }

    @Test
    public void testQuantityLengthRefactored_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    @Test
    public void testBackwardCompatibility_UC1EqualityTests() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(a.isEqual(b));
    }

    @Test
    public void testBackwardCompatibility_UC5ConversionTests() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        assertTrue(a.isEqual(b));
    }

    @Test
    public void testBackwardCompatibility_UC6AdditionTests() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(a, b);
        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    public void testBackwardCompatibility_UC7AdditionWithTargetUnitTests() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(a, b, LengthUnit.INCHES);
        assertEquals(LengthUnit.INCHES, result.getUnit());
        assertEquals(24.0, result.getValue(), EPSILON);
    }

    @Test
    public void testRoundTripConversion_RefactoredDesign() {
        double original = 5.0;
        double toInches = LengthUnit.INCHES.convertFromBaseUnit(LengthUnit.FEET.convertToBaseUnit(original));
        double backToFeet = LengthUnit.FEET.convertFromBaseUnit(LengthUnit.INCHES.convertToBaseUnit(toInches));
        assertEquals(original, backToFeet, EPSILON);
    }

    @Test
    public void testUnitImmutability() {
        LengthUnit feet1 = LengthUnit.FEET;
        LengthUnit feet2 = LengthUnit.FEET;
        assertSame(feet1, feet2);
    }

    @Test
    public void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(a, b);
        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_CrossUnit_YardPlusFeet() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(a, b);
        assertEquals(LengthUnit.YARDS, result.getUnit());
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_NullOperand_ThrowsException() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.add(a, null));
    }

    @Test
    public void testAddition_NullTargetUnit_ThrowsException() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.add(a, b, null));
    }

    @Test
    public void testIsGreaterThan() {
        QuantityLength a = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(a.isGreaterThan(b));
    }

    @Test
    public void testIsLessThan() {
        QuantityLength a = new QuantityLength(6.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(a.isLessThan(b));
    }

    @Test
    public void testInfiniteValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }
}