package org.quantitymeasurement;
import java.util.Objects;

public class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("WeightUnit must not be null.");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be a finite number.");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    public double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit must not be null.");
        double converted = Math.round(targetUnit.convertFromBaseUnit(unit.convertToBaseUnit(value)) * 100000.0) / 100000.0;
        return new QuantityWeight(converted, targetUnit);
    }

    public boolean isEqual(QuantityWeight other) {
        if (other == null)
            throw new IllegalArgumentException("Operand must not be null.");
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < 1e-6;
    }

    public static QuantityWeight add(QuantityWeight weight1, QuantityWeight weight2) {
        if (weight1 == null || weight2 == null)
            throw new IllegalArgumentException("Operands must not be null.");
        return performAddition(weight1, weight2, weight1.unit);
    }

    public static QuantityWeight add(QuantityWeight weight1, QuantityWeight weight2, WeightUnit targetUnit) {
        if (weight1 == null || weight2 == null)
            throw new IllegalArgumentException("Operands must not be null.");
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit must not be null.");
        return performAddition(weight1, weight2, targetUnit);
    }

    private static QuantityWeight performAddition(QuantityWeight weight1, QuantityWeight weight2, WeightUnit targetUnit) {
        double sumInBase = weight1.toBaseUnit() + weight2.toBaseUnit();
        double resultValue = Math.round(targetUnit.convertFromBaseUnit(sumInBase) * 100000.0) / 100000.0;
        return new QuantityWeight(resultValue, targetUnit);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuantityWeight other = (QuantityWeight) obj;
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(toBaseUnit() * 1e6));
    }
}
