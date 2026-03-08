package org.quantitymeasurement;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        System.out.println("Input:  add(" + a + ", " + b + ", FEET)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.FEET));
        System.out.println();

        System.out.println("Input:  add(" + a + ", " + b + ", INCHES)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.INCHES));
        System.out.println();

        System.out.println("Input:  add(" + a + ", " + b + ", YARDS)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.YARDS));
        System.out.println();

        a = new QuantityLength(1.0, LengthUnit.YARDS);
        b = new QuantityLength(3.0, LengthUnit.FEET);
        System.out.println("Input:  add(" + a + ", " + b + ", YARDS)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.YARDS));
        System.out.println();

        a = new QuantityLength(36.0, LengthUnit.INCHES);
        b = new QuantityLength(1.0, LengthUnit.YARDS);
        System.out.println("Input:  add(" + a + ", " + b + ", FEET)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.FEET));
        System.out.println();

        a = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        b = new QuantityLength(1.0, LengthUnit.INCHES);
        System.out.println("Input:  add(" + a + ", " + b + ", CENTIMETERS)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.CENTIMETERS));
        System.out.println();

        a = new QuantityLength(5.0, LengthUnit.FEET);
        b = new QuantityLength(0.0, LengthUnit.INCHES);
        System.out.println("Input:  add(" + a + ", " + b + ", YARDS)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.YARDS));
        System.out.println();

        a = new QuantityLength(5.0, LengthUnit.FEET);
        b = new QuantityLength(-2.0, LengthUnit.FEET);
        System.out.println("Input:  add(" + a + ", " + b + ", INCHES)");
        System.out.println("Output: " + QuantityLength.add(a, b, LengthUnit.INCHES));
    }
}