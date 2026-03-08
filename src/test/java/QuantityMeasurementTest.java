import org.quantitymeasurement.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementTest {

    private static final double EPSILON = 1e-4;

    @Test
    public void testEquality_KilogramToKilogram_SameValue() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertTrue(a.equals(b));
    }

    @Test
    public void testEquality_KilogramToKilogram_DifferentValue() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(2.0, WeightUnit.KILOGRAM);
        assertFalse(a.equals(b));
    }

    @Test
    public void testEquality_KilogramToGram_EquivalentValue() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);
        assertTrue(a.equals(b));
    }

    @Test
    public void testEquality_GramToKilogram_EquivalentValue() {
        QuantityWeight a = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight b = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertTrue(a.equals(b));
    }

    @Test
    public void testEquality_KilogramToPound_EquivalentValue() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(2.20462, WeightUnit.POUND);
        assertTrue(a.equals(b));
    }

    @Test
    public void testEquality_GramToPound_EquivalentValue() {
        QuantityWeight a = new QuantityWeight(453.592, WeightUnit.GRAM);
        QuantityWeight b = new QuantityWeight(1.0, WeightUnit.POUND);
        assertTrue(a.equals(b));
    }

    @Test
    public void testEquality_WeightVsLength_Incompatible() {
        QuantityWeight w = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityLength l = new QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(w.equals(l));
    }

    @Test
    public void testEquality_NullComparison() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertFalse(a.equals(null));
    }

    @Test
    public void testEquality_SameReference() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertTrue(a.equals(a));
    }

    @Test
    public void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityWeight(1.0, null));
    }

    @Test
    public void testEquality_TransitiveProperty() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight c = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    public void testEquality_ZeroValue() {
        QuantityWeight a = new QuantityWeight(0.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(0.0, WeightUnit.GRAM);
        assertTrue(a.equals(b));
    }

    @Test
    public void testEquality_NegativeWeight() {
        QuantityWeight a = new QuantityWeight(-1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(-1000.0, WeightUnit.GRAM);
        assertTrue(a.equals(b));
    }

    @Test
    public void testEquality_LargeWeightValue() {
        QuantityWeight a = new QuantityWeight(1000000.0, WeightUnit.GRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.KILOGRAM);
        assertTrue(a.equals(b));
    }

    @Test
    public void testEquality_SmallWeightValue() {
        QuantityWeight a = new QuantityWeight(0.001, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1.0, WeightUnit.GRAM);
        assertTrue(a.equals(b));
    }

    @Test
    public void testConversion_KilogramToGram() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight result = a.convertTo(WeightUnit.GRAM);
        assertEquals(WeightUnit.GRAM, result.getUnit());
        assertEquals(1000.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_PoundToKilogram() {
        QuantityWeight a = new QuantityWeight(2.20462, WeightUnit.POUND);
        QuantityWeight result = a.convertTo(WeightUnit.KILOGRAM);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_KilogramToPound() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight result = a.convertTo(WeightUnit.POUND);
        assertEquals(WeightUnit.POUND, result.getUnit());
        assertEquals(2.20462, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_GramToPound() {
        QuantityWeight a = new QuantityWeight(500.0, WeightUnit.GRAM);
        QuantityWeight result = a.convertTo(WeightUnit.POUND);
        assertEquals(WeightUnit.POUND, result.getUnit());
        assertEquals(1.10231, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_SameUnit() {
        QuantityWeight a = new QuantityWeight(5.0, WeightUnit.KILOGRAM);
        QuantityWeight result = a.convertTo(WeightUnit.KILOGRAM);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_ZeroValue() {
        QuantityWeight a = new QuantityWeight(0.0, WeightUnit.KILOGRAM);
        QuantityWeight result = a.convertTo(WeightUnit.GRAM);
        assertEquals(WeightUnit.GRAM, result.getUnit());
        assertEquals(0.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_NegativeValue() {
        QuantityWeight a = new QuantityWeight(-1.0, WeightUnit.KILOGRAM);
        QuantityWeight result = a.convertTo(WeightUnit.GRAM);
        assertEquals(WeightUnit.GRAM, result.getUnit());
        assertEquals(-1000.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_RoundTrip() {
        QuantityWeight a = new QuantityWeight(1.5, WeightUnit.KILOGRAM);
        QuantityWeight result = a.convertTo(WeightUnit.GRAM).convertTo(WeightUnit.KILOGRAM);
        assertEquals(1.5, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_SameUnit_KilogramPlusKilogram() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(2.0, WeightUnit.KILOGRAM);
        QuantityWeight result = QuantityWeight.add(a, b);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_CrossUnit_KilogramPlusGram() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight result = QuantityWeight.add(a, b);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_CrossUnit_PoundPlusKilogram() {
        QuantityWeight a = new QuantityWeight(2.20462, WeightUnit.POUND);
        QuantityWeight b = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight result = QuantityWeight.add(a, b);
        assertEquals(WeightUnit.POUND, result.getUnit());
        assertEquals(4.40924, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Gram() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight result = QuantityWeight.add(a, b, WeightUnit.GRAM);
        assertEquals(WeightUnit.GRAM, result.getUnit());
        assertEquals(2000.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Kilogram() {
        QuantityWeight a = new QuantityWeight(2.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(4.0, WeightUnit.POUND);
        QuantityWeight result = QuantityWeight.add(a, b, WeightUnit.KILOGRAM);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
        assertEquals(3.81437, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_Commutativity() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight result1 = QuantityWeight.add(a, b, WeightUnit.KILOGRAM);
        QuantityWeight result2 = QuantityWeight.add(b, a, WeightUnit.KILOGRAM);
        assertEquals(result1.getValue(), result2.getValue(), EPSILON);
    }

    @Test
    public void testAddition_WithZero() {
        QuantityWeight a = new QuantityWeight(5.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(0.0, WeightUnit.GRAM);
        QuantityWeight result = QuantityWeight.add(a, b);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_NegativeValues() {
        QuantityWeight a = new QuantityWeight(5.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(-2000.0, WeightUnit.GRAM);
        QuantityWeight result = QuantityWeight.add(a, b);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_LargeValues() {
        QuantityWeight a = new QuantityWeight(1e6, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1e6, WeightUnit.KILOGRAM);
        QuantityWeight result = QuantityWeight.add(a, b);
        assertEquals(2e6, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_NullOperand_ThrowsException() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> QuantityWeight.add(a, null));
    }

    @Test
    public void testAddition_NullTargetUnit_ThrowsException() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> QuantityWeight.add(a, b, null));
    }

    @Test
    public void testInvalidValue_NaN_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityWeight(Double.NaN, WeightUnit.KILOGRAM));
    }

    @Test
    public void testInvalidValue_Infinite_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityWeight(Double.POSITIVE_INFINITY, WeightUnit.KILOGRAM));
    }

    @Test
    public void testWeightUnit_ConvertToBaseUnit_Gram() {
        assertEquals(0.001, WeightUnit.GRAM.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    public void testWeightUnit_ConvertFromBaseUnit_Gram() {
        assertEquals(1000.0, WeightUnit.GRAM.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    public void testWeightUnit_ConvertToBaseUnit_Pound() {
        assertEquals(0.453592, WeightUnit.POUND.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    public void testWeightUnit_ConvertFromBaseUnit_Pound() {
        assertEquals(2.20462, WeightUnit.POUND.convertFromBaseUnit(1.0), EPSILON);
    }
}