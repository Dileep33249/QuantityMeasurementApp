package org.quantitymeasurement;
public class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    private static final double DELTA = 1e-6;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }
    private double convertToInches() {
        return this.value * this.unit.getConversionFactorToInches();
    }

    /**
     * Compares this QuantityLength with another for equality across units.
     * Both values are converted to inches before comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuantityLength other = (QuantityLength) obj;
        return Math.abs(this.convertToInches() - other.convertToInches()) < DELTA;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(Math.round(convertToInches() / DELTA) * DELTA);
        return (int) (bits ^ (bits >>> 32));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }
}
