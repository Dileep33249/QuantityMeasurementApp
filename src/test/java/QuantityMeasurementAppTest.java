import org.quantitymeasurement.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    @Test
    void testFeetEqualitySameValue() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(5.0);
        QuantityMeasurementApp.Feet feet2 = new QuantityMeasurementApp.Feet(5.0);

        assertTrue(feet1.equals(feet2));
    }

    @Test
    void testFeetInequalityDifferentValue() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(5.0);
        QuantityMeasurementApp.Feet feet2 = new QuantityMeasurementApp.Feet(6.0);

        assertFalse(feet1.equals(feet2));
    }

    @Test
    void testFeetEqualitySameReference() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(5.0);

        assertTrue(feet1.equals(feet1));
    }

    @Test
    void testFeetComparisonWithNull() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(5.0);

        assertFalse(feet1.equals(null));
    }

    @Test
    void testFeetComparisonWithDifferentObject() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(5.0);
        String str = "test";

        assertFalse(feet1.equals(str));
    }
}