package org.quantitymeasurement;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        demonstrateLengthMeasurements();
        demonstrateWeightMeasurements();
        demonstrateVolumeMeasurements();
        demonstrateCategoryIncompatibility();
        demonstrateSubtractionOperations();
        demonstrateDivisionOperations();
    }

    private static void demonstrateLengthMeasurements() {
        System.out.println("--- Length Measurements ---");

        Quantity<LengthUnit> oneFoot        = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> twelveInches   = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> oneYard        = new Quantity<>(1.0, LengthUnit.YARD);

        demonstrateEquality("1 foot == 12 inches",    oneFoot, twelveInches);
        demonstrateEquality("1 yard == 3 feet",       oneYard, new Quantity<>(3.0, LengthUnit.FEET));

        demonstrateConversion("1 foot → inches",      oneFoot, LengthUnit.INCH);
        demonstrateConversion("1 yard → feet",        oneYard, LengthUnit.FEET);

        demonstrateAddition("1 foot + 12 inches",     oneFoot, twelveInches, LengthUnit.FEET);
        demonstrateAddition("1 yard + 3 feet → yards",oneYard, new Quantity<>(3.0, LengthUnit.FEET), LengthUnit.YARD);

        System.out.println();
    }

    private static void demonstrateWeightMeasurements() {
        System.out.println("--- Weight Measurements ---");

        Quantity<WeightUnit> oneKg     = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> thousandG = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> onePound  = new Quantity<>(1.0, WeightUnit.POUND);

        demonstrateEquality("1 kg == 1000 g",         oneKg, thousandG);
        demonstrateEquality("1 kg != 1 pound",        oneKg, onePound);

        demonstrateConversion("1 kg → grams",          oneKg, WeightUnit.GRAM);
        demonstrateConversion("1 pound → kilograms",   onePound, WeightUnit.KILOGRAM);

        demonstrateAddition("1 kg + 1000 g → kg",     oneKg, thousandG, WeightUnit.KILOGRAM);
        demonstrateAddition("1 kg + 1 pound → grams", oneKg, onePound, WeightUnit.GRAM);

        System.out.println();
    }

    private static void demonstrateVolumeMeasurements() {
        System.out.println("--- Volume Measurements (UC11) ---");

        Quantity<VolumeUnit> oneLitre   = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> thousandMl = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> oneGallon  = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> galInLitres = new Quantity<>(3.78541, VolumeUnit.LITRE);

        demonstrateEquality("1 L == 1000 mL",                oneLitre, thousandMl);
        demonstrateEquality("3.78541 L == 1 gallon",          galInLitres, oneGallon);
        demonstrateEquality("1000 mL == 1 L (symmetric)",    thousandMl, oneLitre);

        demonstrateConversion("1 L → mL",            oneLitre, VolumeUnit.MILLILITRE);
        demonstrateConversion("2 gallons → litres",  new Quantity<>(2.0, VolumeUnit.GALLON), VolumeUnit.LITRE);
        demonstrateConversion("500 mL → gallons",    new Quantity<>(500.0, VolumeUnit.MILLILITRE), VolumeUnit.GALLON);
        demonstrateConversion("1 L → L (same unit)", oneLitre, VolumeUnit.LITRE);

        demonstrateAddition("1 L + 2 L",                     new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.LITRE);
        demonstrateAddition("1 L + 1000 mL → L",             oneLitre, thousandMl, VolumeUnit.LITRE);
        demonstrateAddition("500 mL + 0.5 L → mL",          new Quantity<>(500.0, VolumeUnit.MILLILITRE), new Quantity<>(0.5, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);
        demonstrateAddition("2 gal + 3.78541 L → gal",       new Quantity<>(2.0, VolumeUnit.GALLON), galInLitres, VolumeUnit.GALLON);

        demonstrateAddition("1 L + 1000 mL → mL",            oneLitre, thousandMl, VolumeUnit.MILLILITRE);
        demonstrateAddition("1 gal + 3.78541 L → gal",       oneGallon, galInLitres, VolumeUnit.GALLON);
        demonstrateAddition("500 mL + 1 L → gal",            new Quantity<>(500.0, VolumeUnit.MILLILITRE), oneLitre, VolumeUnit.GALLON);
        demonstrateAddition("2 L + 4 gal → L",               new Quantity<>(2.0, VolumeUnit.LITRE), new Quantity<>(4.0, VolumeUnit.GALLON), VolumeUnit.LITRE);

        System.out.println();
    }

    private static void demonstrateCategoryIncompatibility() {
        System.out.println("--- Cross-Category Incompatibility ---");

        Quantity<VolumeUnit> oneLitre    = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<LengthUnit> oneFoot     = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> oneKilogram = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        System.out.printf("1 Litre == 1 Foot     : %b (expected false)%n", oneLitre.equals(oneFoot));
        System.out.printf("1 Litre == 1 Kilogram : %b (expected false)%n", oneLitre.equals(oneKilogram));
        System.out.printf("1 Foot  == 1 Kilogram : %b (expected false)%n", oneFoot.equals(oneKilogram));
        System.out.println();
    }

    private static <U extends IMeasurable> void demonstrateEquality(
            String label, Quantity<U> a, Quantity<U> b) {
        System.out.printf("[Equality]    %-40s : %b%n", label, a.equals(b));
    }

    private static <U extends IMeasurable> void demonstrateConversion(
            String label, Quantity<U> source, U targetUnit) {
        Quantity<U> result = source.convertTo(targetUnit);
        System.out.printf("[Conversion]  %-40s : %s%n", label, result);
    }

    private static <U extends IMeasurable> void demonstrateAddition(
            String label, Quantity<U> a, Quantity<U> b, U targetUnit) {
        Quantity<U> result = a.add(b, targetUnit);
        System.out.printf("[Addition]    %-40s : %s%n", label, result);
    }

    private static void demonstrateSubtractionOperations() {
        System.out.println("--- Subtraction Operations (UC12) ---");

        demonstrateSubtraction("10 ft - 6 in (implicit → ft)",          new Quantity<>(10.0, LengthUnit.FEET),    new Quantity<>(6.0, LengthUnit.INCH),        LengthUnit.FEET);
        demonstrateSubtraction("10 kg - 5000 g (implicit → kg)",        new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5000.0, WeightUnit.GRAM),     WeightUnit.KILOGRAM);
        demonstrateSubtraction("5 L - 500 mL (implicit → L)",           new Quantity<>(5.0, VolumeUnit.LITRE),    new Quantity<>(500.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE);

        demonstrateSubtraction("10 ft - 6 in → in",                     new Quantity<>(10.0, LengthUnit.FEET),    new Quantity<>(6.0, LengthUnit.INCH),        LengthUnit.INCH);
        demonstrateSubtraction("10 kg - 5000 g → g",                    new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5000.0, WeightUnit.GRAM),     WeightUnit.GRAM);
        demonstrateSubtraction("5 L - 2 L → mL",                        new Quantity<>(5.0, VolumeUnit.LITRE),    new Quantity<>(2.0, VolumeUnit.LITRE),        VolumeUnit.MILLILITRE);

        demonstrateSubtraction("5 ft - 10 ft (negative)",               new Quantity<>(5.0, LengthUnit.FEET),     new Quantity<>(10.0, LengthUnit.FEET),       LengthUnit.FEET);
        demonstrateSubtraction("10 ft - 120 in (zero)",                  new Quantity<>(10.0, LengthUnit.FEET),    new Quantity<>(120.0, LengthUnit.INCH),      LengthUnit.FEET);

        System.out.println();
    }

    private static void demonstrateDivisionOperations() {
        System.out.println("--- Division Operations (UC12) ---");

        demonstrateDivision("10 ft ÷ 2 ft",          new Quantity<>(10.0, LengthUnit.FEET),     new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision("24 in ÷ 2 ft",          new Quantity<>(24.0, LengthUnit.INCH),     new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision("10 kg ÷ 5 kg",          new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5.0, WeightUnit.KILOGRAM));
        demonstrateDivision("2000 g ÷ 1 kg",         new Quantity<>(2000.0, WeightUnit.GRAM),   new Quantity<>(1.0, WeightUnit.KILOGRAM));
        demonstrateDivision("5 L ÷ 10 L",            new Quantity<>(5.0, VolumeUnit.LITRE),     new Quantity<>(10.0, VolumeUnit.LITRE));
        demonstrateDivision("1000 mL ÷ 1 L",         new Quantity<>(1000.0, VolumeUnit.MILLILITRE), new Quantity<>(1.0, VolumeUnit.LITRE));

        System.out.println();
    }

    private static <U extends IMeasurable> void demonstrateSubtraction(
            String label, Quantity<U> a, Quantity<U> b, U targetUnit) {
        Quantity<U> result = a.subtract(b, targetUnit);
        System.out.printf("[Subtraction] %-40s : %s%n", label, result);
    }

    private static <U extends IMeasurable> void demonstrateDivision(
            String label, Quantity<U> a, Quantity<U> b) {
        double result = a.divide(b);
        System.out.printf("[Division]    %-40s : %.6f%n", label, result);
    }
}