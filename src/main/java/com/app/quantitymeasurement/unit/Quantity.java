package com.app.quantitymeasurement.unit;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
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
        if (targetUnit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!targetUnit.getClass().equals(unit.getClass())) {
            throw new IllegalArgumentException("Target unit should belong to same class");
        }
        double baseValue = unit.convertToBaseUnit(value);
        return new Quantity<>(round(targetUnit.convertFromBaseUnit(baseValue)), targetUnit);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != getClass()) {
            return false;
        }
        Quantity<?> other = (Quantity<?>) otherObject;
        if (!unit.getClass().equals(other.unit.getClass())) {
            return false;
        }
        return Double.compare(unit.convertToBaseUnit(value), other.unit.convertToBaseUnit(other.value)) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(unit.convertToBaseUnit(value));
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        return buildQuantityFromBase(performBaseArithmetic(other, targetUnit, ArithmeticOperation.ADD, true), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        return buildQuantityFromBase(performBaseArithmetic(other, targetUnit, ArithmeticOperation.SUBTRACT, true), targetUnit);
    }

    public double divide(Quantity<U> other) {
        return performBaseArithmetic(other, null, ArithmeticOperation.DIVIDE, false);
    }

    private double performBaseArithmetic(Quantity<U> other, U targetUnit, ArithmeticOperation operation, boolean targetUnitRequired) {
        validateArithmeticOperands(other, targetUnit, targetUnitRequired);
        return operation.compute(unit.convertToBaseUnit(value), other.unit.convertToBaseUnit(other.value));
    }

    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {
        if (other == null) {
            throw new IllegalArgumentException("Operand cannot be null");
        }
        if (!Double.isFinite(value) || !Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Values must be finite");
        }
        if (!unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cross category operation not allowed");
        }
        if (targetUnitRequired) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            if (!unit.getClass().equals(targetUnit.getClass())) {
                throw new IllegalArgumentException("Invalid target unit category");
            }
        }
    }

    private Quantity<U> buildQuantityFromBase(double baseValue, U targetUnit) {
        return new Quantity<>(round(targetUnit.convertFromBaseUnit(baseValue)), targetUnit);
    }

    private double round(double number) {
        return (double) Math.round(number * 100) / 100;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit);
    }

    private enum ArithmeticOperation {
        ADD {
            @Override
            double compute(double first, double second) {
                return first + second;
            }
        },
        SUBTRACT {
            @Override
            double compute(double first, double second) {
                return first - second;
            }
        },
        DIVIDE {
            @Override
            double compute(double first, double second) {
                if (second == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return first / second;
            }
        };

        abstract double compute(double first, double second);
    }
}
