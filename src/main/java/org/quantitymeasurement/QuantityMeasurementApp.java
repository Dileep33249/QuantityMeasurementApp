package org.quantitymeasurement;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("Input:  Quantity(1.0, FEET).convertTo(INCHES)");
        System.out.println("Output: " + q1.convertTo(LengthUnit.INCHES));
        System.out.println();

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        System.out.println("Input:  Quantity(1.0, FEET).add(Quantity(12.0, INCHES), FEET)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.FEET));
        System.out.println();

        QuantityLength c = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength d = new QuantityLength(1.0, LengthUnit.YARDS);
        System.out.println("Input:  Quantity(36.0, INCHES).equals(Quantity(1.0, YARDS))");
        System.out.println("Output: " + c.equals(d));
        System.out.println();

        QuantityLength e = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength f = new QuantityLength(3.0, LengthUnit.FEET);
        System.out.println("Input:  Quantity(1.0, YARDS).add(Quantity(3.0, FEET), YARDS)");
        System.out.println("Output: " + QuantityLength.add(e, f, LengthUnit.YARDS));
        System.out.println();

        QuantityLength g = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        System.out.println("Input:  Quantity(2.54, CENTIMETERS).convertTo(INCHES)");
        System.out.println("Output: " + g.convertTo(LengthUnit.INCHES));
        System.out.println();

        QuantityLength h = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength i = new QuantityLength(0.0, LengthUnit.INCHES);
        System.out.println("Input:  Quantity(5.0, FEET).add(Quantity(0.0, INCHES), FEET)");
        System.out.println("Output: " + QuantityLength.add(h, i, LengthUnit.FEET));
        System.out.println();

        System.out.println("Input:  LengthUnit.FEET.convertToBaseUnit(12.0)");
        System.out.println("Output: " + LengthUnit.FEET.convertToBaseUnit(12.0));
        System.out.println();

        System.out.println("Input:  LengthUnit.INCHES.convertToBaseUnit(12.0)");
        System.out.println("Output: " + LengthUnit.INCHES.convertToBaseUnit(12.0));
    }
}