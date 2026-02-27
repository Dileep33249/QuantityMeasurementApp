package org.quantitymeasurement;

public class QuantityMeasurementApp {

    public static boolean compareFeet(double v1, double v2) {
        Feet f1 = new Feet(v1);
        Feet f2 = new Feet(v2);
        return f1.equals(f2);
    }

    public static boolean compareInches(double v1, double v2) {
        Inches i1 = new Inches(v1);
        Inches i2 = new Inches(v2);
        return i1.equals(i2);
    }

    public static void main(String[] args) {

        System.out.println("Feet Equality: "
                + compareFeet(1.0, 1.0));

        System.out.println("Inches Equality: "
                + compareInches(1.0, 1.0));
    }
}