package org.quantitymeasurement.unit;

import java.util.Objects;

public class Quantity<U extends Enum<U> & IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        Objects.requireNonNull(unit, "Unit cannot be null");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (!isSameMeasurementType(unit, targetUnit)) {
            throw new IllegalArgumentException("Cannot convert between different measurement types");
        }
        double baseValue = unit.convertToBaseUnit(value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
        return new Quantity<>(convertedValue, targetUnit);
    }

    public boolean equals(Quantity<U> other) {
        if (other == null) {
            return false;
        }
        if (!isSameMeasurementType(unit, other.unit)) {
            throw new IllegalArgumentException("Cannot compare different measurement types");
        }
        double thisBaseValue = unit.convertToBaseUnit(value);
        double otherBaseValue = other.unit.convertToBaseUnit(other.value);
        return Double.compare(thisBaseValue, otherBaseValue) == 0;
    }

    public int compareTo(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare with null");
        }
        if (!isSameMeasurementType(unit, other.unit)) {
            throw new IllegalArgumentException("Cannot compare different measurement types");
        }
        double thisBaseValue = unit.convertToBaseUnit(value);
        double otherBaseValue = other.unit.convertToBaseUnit(other.value);
        return Double.compare(thisBaseValue, otherBaseValue);
    }

    private boolean isSameMeasurementType(U unit1, U unit2) {
        return unit1.getMeasurementType().equals(unit2.getMeasurementType());
    }

    public Quantity<U> add(Quantity<U> other) {
        validateArithmeticOperands(other);
        double otherValueInThisUnit = unit.convertFromBaseUnit(other.unit.convertToBaseUnit(other.value));
        return performBaseArithmetic(new Quantity<>(value + otherValueInThisUnit, unit), null);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        validateArithmeticOperands(other);
        double otherValueInThisUnit = unit.convertFromBaseUnit(other.unit.convertToBaseUnit(other.value));
        return performBaseArithmetic(new Quantity<>(value - otherValueInThisUnit, unit), null);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other);
        double thisBaseValue = unit.convertToBaseUnit(value);
        double otherBaseValue = other.unit.convertToBaseUnit(other.value);
        return ArithmeticOperation.DIVIDE.compute(thisBaseValue, otherBaseValue);
    }

    private void validateArithmeticOperands(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add null quantity");
        }
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("This quantity has NaN value");
        }
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException("This quantity has Infinite value");
        }
        if (Double.isNaN(other.value)) {
            throw new IllegalArgumentException("Other quantity has NaN value");
        }
        if (Double.isInfinite(other.value)) {
            throw new IllegalArgumentException("Other quantity has Infinite value");
        }
        if (!isSameMeasurementType(unit, other.unit)) {
            throw new IllegalArgumentException("Cannot add different measurement types");
        }
    }

    private Quantity<U> performBaseArithmetic(Quantity<U> result, ArithmeticOperation operation) {
        if (result != null) {
            double rounded = Math.round(result.getValue() * 100.0) / 100.0;
            return new Quantity<>(rounded, result.getUnit());
        }
        return result;
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        validateArithmeticOperands(other);
        double otherValueInThisUnit = unit.convertFromBaseUnit(other.unit.convertToBaseUnit(other.value));
        double sumValue = value + otherValueInThisUnit;
        Quantity<U> resultInThisUnit = new Quantity<>(sumValue, unit);
        Quantity<U> resultInTargetUnit = resultInThisUnit.convertTo(targetUnit);
        // Round to 2 decimal places for final result
        double rounded = Math.round(resultInTargetUnit.getValue() * 100.0) / 100.0;
        return new Quantity<>(rounded, resultInTargetUnit.getUnit());
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        validateArithmeticOperands(other);
        double otherValueInThisUnit = unit.convertFromBaseUnit(other.unit.convertToBaseUnit(other.value));
        double diffValue = value - otherValueInThisUnit;
        Quantity<U> resultInThisUnit = new Quantity<>(diffValue, unit);
        Quantity<U> resultInTargetUnit = resultInThisUnit.convertTo(targetUnit);
        double rounded = Math.round(resultInTargetUnit.getValue() * 100.0) / 100.0;
        return new Quantity<>(rounded, resultInTargetUnit.getUnit());
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof Quantity))
            return false;
        Quantity<?> other = (Quantity<?>) obj;
        return Objects.equals(unit.getMeasurementType(), ((IMeasurable) other.unit).getMeasurementType())
                && Double.compare(unit.convertToBaseUnit(value),
                        ((IMeasurable) other.unit).convertToBaseUnit(other.value)) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.convertToBaseUnit(value), unit.getMeasurementType());
    }
}
