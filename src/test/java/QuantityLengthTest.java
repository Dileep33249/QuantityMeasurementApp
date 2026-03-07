import org.junit.jupiter.api.Test;
import org.quantitymeasurement.*;
import org.junit.jupiter.api.Assertions;

public class QuantityLengthTest {

    @Test
    void testEquality_FeetToFeet_SameValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_InchesToInches_SameValue() {
        QuantityLength a = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_FeetToInches_EquivalentValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_InchesToFeet_EquivalentValue() {
        QuantityLength a = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        Assertions.assertTrue(a.equals(b));
    }

    // ─────────────────────────────────────────────
    // UC4: Yard-to-Yard Equality
    // ─────────────────────────────────────────────

    @Test
    void testEquality_YardToYard_SameValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.YARDS);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.YARDS);
        Assertions.assertFalse(a.equals(b));
    }

    // ─────────────────────────────────────────────
    // UC4: Yard Cross-Unit Conversions
    // ─────────────────────────────────────────────

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        QuantityLength a = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.YARDS);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(36.0, LengthUnit.INCHES);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        QuantityLength a = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.YARDS);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.FEET);
        Assertions.assertFalse(a.equals(b));
    }

    @Test
    void testEquality_CentimetersToCentimeters_SameValue() {
        QuantityLength a = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_CentimetersToCentimeters_DifferentValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        Assertions.assertFalse(a.equals(b));
    }

    @Test
    void testEquality_centimetersToInches_EquivalentValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength b = new QuantityLength(0.393701, LengthUnit.INCHES);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_InchesToCentimeters_EquivalentValue() {
        QuantityLength a = new QuantityLength(0.393701, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        Assertions.assertTrue(a.equals(b));
    }

    @Test
    void testEquality_centimetersToFeet_NonEquivalentValue() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);
        Assertions.assertFalse(a.equals(b));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        QuantityLength yards   = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet    = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches  = new QuantityLength(36.0, LengthUnit.INCHES);

        // A == B, B == C, therefore A == C
        Assertions.assertTrue(yards.equals(feet));
        Assertions.assertTrue(feet.equals(inches));
        Assertions.assertTrue(yards.equals(inches));
    }

    @Test
    void testEquality_AllUnits_ComplexScenario() {
        QuantityLength twoYards      = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength sixFeet       = new QuantityLength(6.0, LengthUnit.FEET);
        QuantityLength seventyTwoIn  = new QuantityLength(72.0, LengthUnit.INCHES);

        Assertions.assertTrue(twoYards.equals(sixFeet));
        Assertions.assertTrue(sixFeet.equals(seventyTwoIn));
        Assertions.assertTrue(twoYards.equals(seventyTwoIn));
    }

    @Test
    void testEquality_YardSameReference() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        Assertions.assertTrue(a.equals(a));
    }

    @Test
    void testEquality_YardNullComparison() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        Assertions.assertFalse(a.equals(null));
    }

    @Test
    void testEquality_YardWithNullUnit() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new QuantityLength(1.0, null)
        );
    }

    @Test
    void testEquality_CentimetersSameReference() {
        QuantityLength a = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        Assertions.assertTrue(a.equals(a));
    }

    @Test
    void testEquality_CentimetersNullComparison() {
        QuantityLength a = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        Assertions.assertFalse(a.equals(null));
    }

    @Test
    void testEquality_CentimetersWithNullUnit() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new QuantityLength(1.0, null)
        );
    }
}