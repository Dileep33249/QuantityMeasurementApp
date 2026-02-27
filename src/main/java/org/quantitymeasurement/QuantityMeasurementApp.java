package org.quantitymeasurement;
public class QuantityMeasurementApp {

    public static boolean compare(
            double v1, LengthUnit u1,
            double v2, LengthUnit u2) {

        QuantityLength q1 =
                new QuantityLength(v1, u1);

        QuantityLength q2 =
                new QuantityLength(v2, u2);

        return q1.equals(q2);
    }

    public static void main(String[] args) {

        System.out.println(
                compare(1.0, LengthUnit.FEET,
                        12.0, LengthUnit.INCH));

        System.out.println(
                compare(1.0, LengthUnit.INCH,
                        1.0, LengthUnit.INCH));
    }
}