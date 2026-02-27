package org.quantitymeasurement;

public class Inches {

    private final double value;

    public Inches(double value) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException("Invalid inches value");
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Inches inches = (Inches) obj;
        return Double.compare(inches.value, this.value) == 0;
    }
}