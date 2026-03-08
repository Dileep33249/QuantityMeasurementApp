package org.quantitymeasurement;
import org.quantitymeasurement.*;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        System.out.println("=== Length Operations ===");
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("Input:  Quantity(1.0, FEET).convertTo(INCHES)");
        System.out.println("Output: " + l1.convertTo(LengthUnit.INCHES));
        System.out.println();

        QuantityLength la = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength lb = new QuantityLength(12.0, LengthUnit.INCHES);
        System.out.println("Input:  add(Quantity(1.0, FEET), Quantity(12.0, INCHES), FEET)");
        System.out.println("Output: " + QuantityLength.add(la, lb, LengthUnit.FEET));
        System.out.println();

        System.out.println("=== Weight Equality ===");
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        System.out.println("Input:  Quantity(1.0, KILOGRAM).equals(Quantity(1.0, KILOGRAM))");
        System.out.println("Output: " + w1.equals(w2));
        System.out.println();

        QuantityWeight w3 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w4 = new QuantityWeight(1000.0, WeightUnit.GRAM);
        System.out.println("Input:  Quantity(1.0, KILOGRAM).equals(Quantity(1000.0, GRAM))");
        System.out.println("Output: " + w3.equals(w4));
        System.out.println();

        QuantityWeight w5 = new QuantityWeight(500.0, WeightUnit.GRAM);
        QuantityWeight w6 = new QuantityWeight(0.5, WeightUnit.KILOGRAM);
        System.out.println("Input:  Quantity(500.0, GRAM).equals(Quantity(0.5, KILOGRAM))");
        System.out.println("Output: " + w5.equals(w6));
        System.out.println();

        System.out.println("=== Weight Conversions ===");
        QuantityWeight w7 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        System.out.println("Input:  Quantity(1.0, KILOGRAM).convertTo(GRAM)");
        System.out.println("Output: " + w7.convertTo(WeightUnit.GRAM));
        System.out.println();

        QuantityWeight w8 = new QuantityWeight(2.0, WeightUnit.POUND);
        System.out.println("Input:  Quantity(2.0, POUND).convertTo(KILOGRAM)");
        System.out.println("Output: " + w8.convertTo(WeightUnit.KILOGRAM));
        System.out.println();

        QuantityWeight w9 = new QuantityWeight(500.0, WeightUnit.GRAM);
        System.out.println("Input:  Quantity(500.0, GRAM).convertTo(POUND)");
        System.out.println("Output: " + w9.convertTo(WeightUnit.POUND));
        System.out.println();

        System.out.println("=== Weight Addition (Implicit Target) ===");
        QuantityWeight wa1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight wa2 = new QuantityWeight(2.0, WeightUnit.KILOGRAM);
        System.out.println("Input:  add(Quantity(1.0, KILOGRAM), Quantity(2.0, KILOGRAM))");
        System.out.println("Output: " + QuantityWeight.add(wa1, wa2));
        System.out.println();

        QuantityWeight wa3 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight wa4 = new QuantityWeight(1000.0, WeightUnit.GRAM);
        System.out.println("Input:  add(Quantity(1.0, KILOGRAM), Quantity(1000.0, GRAM))");
        System.out.println("Output: " + QuantityWeight.add(wa3, wa4));
        System.out.println();

        System.out.println("=== Weight Addition (Explicit Target) ===");
        QuantityWeight wa5 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight wa6 = new QuantityWeight(1000.0, WeightUnit.GRAM);
        System.out.println("Input:  add(Quantity(1.0, KILOGRAM), Quantity(1000.0, GRAM), GRAM)");
        System.out.println("Output: " + QuantityWeight.add(wa5, wa6, WeightUnit.GRAM));
        System.out.println();

        QuantityWeight wa7 = new QuantityWeight(2.0, WeightUnit.KILOGRAM);
        QuantityWeight wa8 = new QuantityWeight(4.0, WeightUnit.POUND);
        System.out.println("Input:  add(Quantity(2.0, KILOGRAM), Quantity(4.0, POUND), KILOGRAM)");
        System.out.println("Output: " + QuantityWeight.add(wa7, wa8, WeightUnit.KILOGRAM));
        System.out.println();

        System.out.println("=== Category Incompatibility ===");
        QuantityWeight wc = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityLength lc = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("Input:  Quantity(1.0, KILOGRAM).equals(Quantity(1.0, FEET))");
        System.out.println("Output: " + wc.equals(lc));
    }
}