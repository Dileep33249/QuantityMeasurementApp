package org.quantitymeasurement;
import java.util.Objects;

import java.util.Objects;

import java.util.Objects;

import java.util.Objects;

public class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("LengthUnit must not be null.");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be a finite number.");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    public double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit must not be null.");
        double converted = Math.round(targetUnit.convertFromBaseUnit(unit.convertToBaseUnit(value)) * 100.0) / 100.0;
        return new QuantityLength(converted, targetUnit);
    }

    public boolean isEqual(QuantityLength other) {
        if (other == null)
            throw new IllegalArgumentException("Operand must not be null.");
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < 1e-9;
    }

    public boolean isGreaterThan(QuantityLength other) {
        if (other == null)
            throw new IllegalArgumentException("Operand must not be null.");
        return this.toBaseUnit() > other.toBaseUnit();
    }

    public boolean isLessThan(QuantityLength other) {
        if (other == null)
            throw new IllegalArgumentException("Operand must not be null.");
        return this.toBaseUnit() < other.toBaseUnit();
    }

    public static QuantityLength add(QuantityLength length1, QuantityLength length2) {
        if (length1 == null || length2 == null)
            throw new IllegalArgumentException("Operands must not be null.");
        return performAddition(length1, length2, length1.unit);
    }

    public static QuantityLength add(QuantityLength length1, QuantityLength length2, LengthUnit targetUnit) {
        if (length1 == null || length2 == null)
            throw new IllegalArgumentException("Operands must not be null.");
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit must not be null.");
        return performAddition(length1, length2, targetUnit);
    }

    private static QuantityLength performAddition(QuantityLength length1, QuantityLength length2, LengthUnit targetUnit) {
        double sumInBase = length1.toBaseUnit() + length2.toBaseUnit();
        double resultValue = Math.round(targetUnit.convertFromBaseUnit(sumInBase) * 100.0) / 100.0;
        return new QuantityLength(resultValue, targetUnit);
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
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < 1e-9;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(toBaseUnit() * 1e9));
    }
}