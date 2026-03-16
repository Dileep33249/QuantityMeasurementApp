package org.quantitymeasurement;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Quantity<U extends Enum<U> & IMeasurable> {

    private final double value;
    private final U      unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit must not be null.");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Quantity value must be a finite number, but received: " + value);
        }
        this.value = value;
        this.unit  = unit;
    }

    public double getValue() { return value; }
    public U      getUnit()  { return unit; }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double converted  = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(roundToTwoDecimals(converted), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double converted  = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(roundToTwoDecimals(converted), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    private void validateArithmeticOperands(Quantity<U> other, U targetUnit,
                                            boolean targetUnitRequired) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "Operand must not be null. A valid Quantity is required for arithmetic.");
        }
        // Use getDeclaringClass() for enums to properly compare enum types
        if (!this.unit.getDeclaringClass().equals(other.unit.getDeclaringClass())) {
            throw new IllegalArgumentException(
                    "Incompatible unit categories: cannot perform arithmetic between '"
                            + this.unit.getDeclaringClass().getSimpleName()
                            + "' and '"
                            + other.unit.getDeclaringClass().getSimpleName()
                            + "'. Both quantities must belong to the same measurement category.");
        }
        if (!Double.isFinite(this.value)) {
            throw new IllegalArgumentException(
                    "This quantity's value is not finite: " + this.value
                            + ". Arithmetic requires finite numeric values.");
        }
        if (!Double.isFinite(other.value)) {
            throw new IllegalArgumentException(
                    "Operand quantity's value is not finite: " + other.value
                            + ". Arithmetic requires finite numeric values.");
        }
        if (targetUnitRequired && targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit must not be null. Please provide a valid unit for the result.");
        }
    }

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        double thisBase  = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);
        return operation.compute(thisBase, otherBase);
    }

    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity)) return false;
        Quantity<?> that = (Quantity<?>) o;
        return Double.compare(that.value, value) == 0
                && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}