import org.junit.jupiter.api.Test;
import org.quantitymeasurement.*;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityLengthTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_SameUnit_InchPlusInch() {
        QuantityLength a = new QuantityLength(6.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(6.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.INCHES, result.getUnit());
        assertEquals(12.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength a = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.INCHES, result.getUnit());
        assertEquals(24.0, result.getValue(), EPSILON);
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
    public void testAddition_CrossUnit_CentimeterPlusInch() {
        QuantityLength a = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.CENTIMETERS, result.getUnit());
        assertEquals(5.08, result.getValue(), 1e-6);
    }

    @Test
    public void testAddition_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result1 = QuantityLength.add(a, b);
        QuantityLength result2 = QuantityLength.add(b, a);

        assertEquals(result1.toFeet(), result2.toFeet(), EPSILON);
    }

    @Test
    public void testAddition_WithZero() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(0.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_NegativeValues() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(-2.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_NullFirstOperand() {
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.add(null, b));
    }

    @Test
    public void testAddition_NullSecondOperand() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.add(a, null));
    }

    @Test
    public void testAddition_LargeValues() {
        QuantityLength a = new QuantityLength(1e6, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(1e6, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(2e6, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_SmallValues() {
        QuantityLength a = new QuantityLength(0.001, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(0.002, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(0.003, result.getValue(), EPSILON);
    }

    @Test
    public void testAddition_InchesAndYards_ResultInInches() {
        QuantityLength a = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength result = QuantityLength.add(a, b);

        assertEquals(LengthUnit.INCHES, result.getUnit());
        assertEquals(72.0, result.getValue(), EPSILON);
    }

    // ─── UC5: Comparison Tests (preserved) ───────────────────────────────────

    @Test
    public void testIsEqual_SameUnit() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(a.isEqual(b));
    }

    @Test
    public void testIsEqual_CrossUnit_FeetAndInches() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        assertTrue(a.isEqual(b));
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
    public void testNullUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    public void testInfiniteValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    @Test
    public void testNaNValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }
}
