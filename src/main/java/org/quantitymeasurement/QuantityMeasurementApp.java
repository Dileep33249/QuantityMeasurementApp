package org.quantitymeasurement;
import java.util.*;
public class QuantityMeasurementApp {
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (this == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            Feet other = (Feet) (obj);
            return Double.compare(this.value, other.value) == 0;
        }
        public static boolean CheckEqualityFeet(double value1 , double value2){
            Feet feet1  = new Feet(value1);
            Feet feet2 = new Feet(value2);

            return feet1.equals(feet2);
        }
    }

    public static void main(String [] args){
        Scanner sc =new Scanner(System.in);

        System.out.println("enter the first value: ");
        Feet FirstValue = new Feet(sc.nextDouble());

        System.out.println("enter the second value: ");
        Feet secondValue = new Feet(sc.nextDouble());

        boolean result  = FirstValue.equals(secondValue);

        System.out.println(result);


    }
}