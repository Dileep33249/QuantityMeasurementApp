package org.quantitymeasurement;
import static org.quantitymeasurement.LengthUnit.*;
import static org.quantitymeasurement.WeightUnit.*;
import static org.quantitymeasurement.VolumeUnit.*;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        printHeader("Addition Operations");
        demonstrateAddition();

        printHeader("Subtraction Operations");
        demonstrateSubtraction();

        printHeader("Division Operations");
        demonstrateDivision();

        printHeader("Error Cases");
        demonstrateErrorCases();
    }

    private static void demonstrateAddition() {
        Quantity<LengthUnit> r1 = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCHES));
        System.out.println("1.0 FEET + 12.0 INCHES  →  " + r1);

        Quantity<WeightUnit> r2 = new Quantity<>(10.0, KILOGRAM).add(new Quantity<>(5000.0, GRAM), GRAM);
        System.out.println("10.0 KILOGRAM + 5000.0 GRAM (→ GRAM)  →  " + r2);

        Quantity<VolumeUnit> r3 = new Quantity<>(1.0, LITRE).add(new Quantity<>(500.0, MILLILITRE), MILLILITRE);
        System.out.println("1.0 LITRE + 500.0 MILLILITRE (→ MILLILITRE)  →  " + r3);
    }

    private static void demonstrateSubtraction() {
        Quantity<LengthUnit> r1 = new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCHES));
        System.out.println("10.0 FEET - 6.0 INCHES  →  " + r1);

        Quantity<VolumeUnit> r2 = new Quantity<>(5.0, LITRE).subtract(new Quantity<>(2.0, LITRE), MILLILITRE);
        System.out.println("5.0 LITRE - 2.0 LITRE (→ MILLILITRE)  →  " + r2);

        Quantity<WeightUnit> r3 = new Quantity<>(1.0, TONNE).subtract(new Quantity<>(500.0, KILOGRAM), GRAM);
        System.out.println("1.0 TONNE - 500.0 KILOGRAM (→ GRAM)  →  " + r3);
    }

    private static void demonstrateDivision() {
        double r1 = new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET));
        System.out.println("10.0 FEET / 2.0 FEET  →  " + r1);

        double r2 = new Quantity<>(24.0, INCHES).divide(new Quantity<>(2.0, FEET));
        System.out.println("24.0 INCHES / 2.0 FEET  →  " + r2);

        double r3 = new Quantity<>(1.0, KILOGRAM).divide(new Quantity<>(500.0, GRAM));
        System.out.println("1.0 KILOGRAM / 500.0 GRAM  →  " + r3);
    }

    private static void demonstrateErrorCases() {
        tryOperation("add(null)",                  () -> new Quantity<>(10.0, FEET).add(null));
        tryOperation("divide by zero",             () -> new Quantity<>(10.0, FEET).divide(new Quantity<>(0.0, FEET)));
        tryOperation("NaN value",                  () -> new Quantity<>(Double.NaN, FEET).add(new Quantity<>(1.0, FEET)));
        tryOperation("null target unit",           () -> new Quantity<>(10.0, FEET).add(new Quantity<>(5.0, INCHES), null));
    }

    private static void tryOperation(String label, Runnable op) {
        try {
            op.run();
            System.out.println("[" + label + "]  (no exception – unexpected!)");
        } catch (IllegalArgumentException | ArithmeticException e) {
            System.out.println("[" + label + "]  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static void printHeader(String title) {
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("  " + title);
        System.out.println("═══════════════════════════════════════════════════");
    }
}