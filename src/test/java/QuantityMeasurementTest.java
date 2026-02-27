import org.quantitymeasurement.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementTest {

    @Test
    void testEquality_SameValue() {
        assertTrue(
                QuantityMeasurementApp.compareFeet(1.0, 1.0));
    }

    @Test
    void testEquality_DifferentValue() {
        assertFalse(
                QuantityMeasurementApp.compareFeet(1.0, 2.0));
    }

    @Test
    void testEquality_SameReference() {
        Feet feet = new Feet(1.0);
        assertEquals(feet, feet);
    }

    @Test
    void testEquality_NullComparison() {
        Feet feet = new Feet(1.0);
        assertNotEquals(feet, null);
    }

    @Test
    void testEquality_InchesSameValue() {
        assertTrue(
                QuantityMeasurementApp.compareInches(2.0, 2.0));
    }

    @Test
    void testEquality_InchesDifferentValue() {
        assertFalse(
                QuantityMeasurementApp.compareInches(2.0, 5.0));
    }

    @Test
    void testEquality_NonNumericInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Feet(Double.NaN));
    }
}