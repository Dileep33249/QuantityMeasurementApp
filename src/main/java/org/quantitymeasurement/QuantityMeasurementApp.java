package org.quantitymeasurement;
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        System.out.println("=== UC4: Extended Unit Support Demo ===\n");

        compare(new QuantityLength(1.0, LengthUnit.YARDS),  new QuantityLength(3.0, LengthUnit.FEET));
        compare(new QuantityLength(1.0, LengthUnit.YARDS),  new QuantityLength(36.0, LengthUnit.INCHES));
        compare(new QuantityLength(2.0, LengthUnit.YARDS),  new QuantityLength(2.0, LengthUnit.YARDS));
        compare(new QuantityLength(2.0, LengthUnit.CENTIMETERS), new QuantityLength(2.0, LengthUnit.CENTIMETERS));
        compare(new QuantityLength(1.0, LengthUnit.CENTIMETERS), new QuantityLength(0.393701, LengthUnit.INCHES));
        compare(new QuantityLength(2.0, LengthUnit.YARDS),  new QuantityLength(6.0, LengthUnit.FEET));
        compare(new QuantityLength(2.0, LengthUnit.YARDS),  new QuantityLength(72.0, LengthUnit.INCHES));
        compare(new QuantityLength(1.0, LengthUnit.FEET),   new QuantityLength(12.0, LengthUnit.INCHES));
    }

    private static void compare(QuantityLength a, QuantityLength b) {
        boolean result = a.equals(b);
        System.out.printf("Input:  %s and %s%n", a, b);
        System.out.printf("Output: %s (%b)%n%n", result ? "Equal" : "Not Equal", result);
    }
}