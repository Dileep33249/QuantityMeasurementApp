package org.quantitymeasurement;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        System.out.println("=== UC6: Addition of Two Length Units ===\n");

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(a, b);
        System.out.println("Input:  add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();

        a = new QuantityLength(1.0, LengthUnit.FEET);
        b = new QuantityLength(12.0, LengthUnit.INCHES);
        result = QuantityLength.add(a, b);
        System.out.println("Input:  add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();

        a = new QuantityLength(12.0, LengthUnit.INCHES);
        b = new QuantityLength(1.0, LengthUnit.FEET);
        result = QuantityLength.add(a, b);
        System.out.println("Input:  add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();

        a = new QuantityLength(1.0, LengthUnit.YARDS);
        b = new QuantityLength(3.0, LengthUnit.FEET);
        result = QuantityLength.add(a, b);
        System.out.println("Input:  add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();


        a = new QuantityLength(36.0, LengthUnit.INCHES);
        b = new QuantityLength(1.0, LengthUnit.YARDS);
        result = QuantityLength.add(a, b);
        System.out.println("Input:  add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();

        a = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        b = new QuantityLength(1.0, LengthUnit.INCHES);
        result = QuantityLength.add(a, b);
        System.out.println("Input:  add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();


        a = new QuantityLength(5.0, LengthUnit.FEET);
        b = new QuantityLength(0.0, LengthUnit.INCHES);
        result = QuantityLength.add(a, b);
        System.out.println("Input:  add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();


        a = new QuantityLength(5.0, LengthUnit.FEET);
        b = new QuantityLength(-2.0, LengthUnit.FEET);
        result = QuantityLength.add(a, b);
        System.out.println("Input:  add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();
    }
}