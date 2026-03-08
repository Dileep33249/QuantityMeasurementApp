package org.quantitymeasurement;
import java.util.Objects;

public class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("LengthUnit must not be null.");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number.");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    // Convert this quantity to feet (base unit)
    public double toFeet() {
        return value * unit.getConversionFactorToFeet();
    }

    // Convert this quantity to a target unit
    public double convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null.");
        }
        double inFeet = this.toFeet();
        return inFeet / targetUnit.getConversionFactorToFeet();
    }

    // Compare two QuantityLength objects (UC5)
    public boolean isEqual(QuantityLength other) {
        if (other == null) {
            throw new IllegalArgumentException("Operand must not be null.");
        }
        double epsilon = 1e-9;
        return Math.abs(this.toFeet() - other.toFeet()) < epsilon;
    }

    public boolean isGreaterThan(QuantityLength other) {
        if (other == null) {
            throw new IllegalArgumentException("Operand must not be null.");
        }
        return this.toFeet() > other.toFeet();
    }

    public boolean isLessThan(QuantityLength other) {
        if (other == null) {
            throw new IllegalArgumentException("Operand must not be null.");
        }
        return this.toFeet() < other.toFeet();
    }

    // UC6: Static add method — result in unit of first operand
    public static QuantityLength add(QuantityLength length1, QuantityLength length2) {
        if (length1 == null || length2 == null) {
            throw new IllegalArgumentException("Operands must not be null.");
        }
        if (!Double.isFinite(length1.value) || !Double.isFinite(length2.value)) {
            throw new IllegalArgumentException("Values must be finite numbers.");
        }

        // Convert both to feet (base unit), sum, then convert to unit of first operand
        double sumInFeet = length1.toFeet() + length2.toFeet();
        double resultValue = sumInFeet / length1.unit.getConversionFactorToFeet();

        return new QuantityLength(resultValue, length1.unit);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityLength)) return false;
        QuantityLength other = (QuantityLength) obj;
        return Double.compare(this.value, other.value) == 0 && this.unit == other.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }
}
