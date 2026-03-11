package org.quantitymeasurement;
public class QuantityMeasurementApp {

    public static void main(String[] args) {

        System.out.println("===== Unit Conversions =====");

        demonstrateLengthConversion(1.0,  LengthUnit.FEET,        LengthUnit.INCHES);
        demonstrateLengthConversion(3.0,  LengthUnit.YARDS,       LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCHES,      LengthUnit.YARDS);
        demonstrateLengthConversion(1.0,  LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        demonstrateLengthConversion(0.0,  LengthUnit.FEET,        LengthUnit.INCHES);
        demonstrateLengthConversion(-1.0, LengthUnit.FEET,        LengthUnit.INCHES);

        System.out.println();
        System.out.println("===== Overloaded: QuantityLength instance → convert =====");

        QuantityLength lengthInYards = new QuantityLength(2.0, LengthUnit.YARDS);
        demonstrateLengthConversion(lengthInYards, LengthUnit.INCHES);

        QuantityLength lengthInCm = new QuantityLength(30.48, LengthUnit.CENTIMETERS);
        demonstrateLengthConversion(lengthInCm, LengthUnit.FEET);

        System.out.println();
        System.out.println("===== Equality Checks =====");

        QuantityLength oneYard     = new QuantityLength(1.0,  LengthUnit.YARDS);
        QuantityLength threeFeet   = new QuantityLength(3.0,  LengthUnit.FEET);
        QuantityLength thirtySixIn = new QuantityLength(36.0, LengthUnit.INCHES);

        demonstrateLengthEquality(oneYard,    threeFeet);
        demonstrateLengthEquality(oneYard,    thirtySixIn);
        demonstrateLengthEquality(threeFeet,  thirtySixIn);

        QuantityLength twoFeet = new QuantityLength(2.0, LengthUnit.FEET);
        demonstrateLengthEquality(oneYard, twoFeet);

        System.out.println();
        System.out.println("===== Comparison via demonstrateLengthComparison =====");

        demonstrateLengthComparison(12.0, LengthUnit.INCHES, 1.0, LengthUnit.FEET);
        demonstrateLengthComparison(1.0,  LengthUnit.YARDS,  2.0, LengthUnit.FEET);

        System.out.println();
        System.out.println("===== Invalid Input Handling =====");

        testInvalidInput();
    }

    public static void demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
        QuantityLength source    = new QuantityLength(value, fromUnit);
        QuantityLength converted = source.convertTo(toUnit);

        System.out.printf("  convert(%.4f, %-12s, %-12s) = %.6f %s%n",
                value,
                fromUnit.name(),
                toUnit.name(),
                converted.getValue(),
                toUnit.name());
    }

    public static void demonstrateLengthConversion(QuantityLength length, LengthUnit targetUnit) {
        QuantityLength converted = length.convertTo(targetUnit);

        System.out.printf("  convert(%s → %-12s) = %.6f %s%n",
                length,
                targetUnit.name(),
                converted.getValue(),
                targetUnit.name());
    }

    public static void demonstrateLengthEquality(QuantityLength a, QuantityLength b) {
        boolean equal = a.equals(b);
        System.out.printf("  %s == %s → %b%n", a, b, equal);
    }

    public static void demonstrateLengthComparison(double value1, LengthUnit unit1,
                                                   double value2, LengthUnit unit2) {
        QuantityLength a = new QuantityLength(value1, unit1);
        QuantityLength b = new QuantityLength(value2, unit2);
        demonstrateLengthEquality(a, b);
    }

    private static void testInvalidInput() {
        try {
            QuantityLength.convert(1.0, null, LengthUnit.INCHES);
            System.out.println("  [FAIL] Null unit should have thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [PASS] Null unit: " + e.getMessage());
        }

        try {
            QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
            System.out.println("  [FAIL] NaN should have thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [PASS] NaN value: " + e.getMessage());
        }

        try {
            QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES);
            System.out.println("  [FAIL] Infinite should have thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [PASS] Infinite value: " + e.getMessage());
        }
    }
}