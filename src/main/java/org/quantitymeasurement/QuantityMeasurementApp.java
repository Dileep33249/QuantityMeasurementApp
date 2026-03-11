package org.quantitymeasurement;
public class QuantityMeasurementApp {

    public static void main(String[] args) {

        System.out.println("===== Length Operations =====");

        Quantity<LengthUnit> oneFoot       = new Quantity<>(1.0,  LengthUnit.FEET);
        Quantity<LengthUnit> twelveInches  = new Quantity<>(12.0, LengthUnit.INCHES);
        Quantity<LengthUnit> oneYard       = new Quantity<>(1.0,  LengthUnit.YARDS);
        Quantity<LengthUnit> thirtySixIn   = new Quantity<>(36.0, LengthUnit.INCHES);

        demonstrateEquality(oneFoot, twelveInches);
        demonstrateEquality(oneYard, thirtySixIn);
        demonstrateConversion(oneFoot, LengthUnit.INCHES);
        demonstrateConversion(oneYard, LengthUnit.FEET);
        demonstrateAddition(oneFoot, twelveInches, LengthUnit.FEET);

        System.out.println();
        System.out.println("===== Weight Operations =====");

        Quantity<WeightUnit> oneKg        = new Quantity<>(1.0,    WeightUnit.KILOGRAM);
        Quantity<WeightUnit> thousandGram = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> onePound     = new Quantity<>(1.0,    WeightUnit.POUND);

        demonstrateEquality(oneKg, thousandGram);
        demonstrateConversion(oneKg, WeightUnit.GRAM);
        demonstrateConversion(onePound, WeightUnit.KILOGRAM);
        demonstrateAddition(oneKg, thousandGram, WeightUnit.KILOGRAM);

        System.out.println();
        System.out.println("===== Cross-Category Prevention =====");

        demonstrateCrossCategoryPrevention();

        System.out.println();
        System.out.println("===== Invalid Input Handling =====");

        demonstrateInvalidInput();
    }

    public static <U extends IMeasurable> void demonstrateEquality(
            Quantity<U> a, Quantity<U> b) {
        System.out.printf("  %s == %s → %b%n", a, b, a.equals(b));
    }

    public static <U extends IMeasurable> void demonstrateConversion(
            Quantity<U> quantity, U targetUnit) {
        Quantity<U> result = quantity.convertTo(targetUnit);
        System.out.printf("  %s → %s%n", quantity, result);
    }

    public static <U extends IMeasurable> void demonstrateAddition(
            Quantity<U> a, Quantity<U> b, U targetUnit) {
        Quantity<U> result = a.add(b, targetUnit);
        System.out.printf("  %s + %s = %s%n", a, b, result);
    }

    private static void demonstrateCrossCategoryPrevention() {
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        System.out.printf("  Quantity<LengthUnit>(1.0 FEET).equals(Quantity<WeightUnit>(1.0 KILOGRAM)) → %b%n",
                length.equals(weight));
    }

    private static void demonstrateInvalidInput() {
        try {
            new Quantity<>(Double.NaN, LengthUnit.FEET);
            System.out.println("  [FAIL] NaN should have thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [PASS] NaN value: " + e.getMessage());
        }

        try {
            new Quantity<>(1.0, null);
            System.out.println("  [FAIL] Null unit should have thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [PASS] Null unit: " + e.getMessage());
        }

        try {
            new Quantity<>(Double.POSITIVE_INFINITY, WeightUnit.GRAM);
            System.out.println("  [FAIL] Infinite should have thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [PASS] Infinite value: " + e.getMessage());
        }
    }
}