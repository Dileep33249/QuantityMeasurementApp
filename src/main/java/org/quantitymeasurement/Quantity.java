package org.quantitymeasurement;

public class Quantity<U extends IMeasurable> {

    private static final double EPSILON = 1e-6;

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit must not be null.");
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Quantity)) return false;

        Quantity<?> other = (Quantity<?>) obj;

        if (!this.unit.getClass().equals(other.unit.getClass())) return false;

        double thisBase  = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return Math.abs(thisBase - otherBase) < EPSILON;
    }

    @Override
    public int hashCode() {
        double baseValue = unit.convertToBaseUnit(value);
        long bits = Double.doubleToLongBits(Math.round(baseValue / EPSILON) * EPSILON);
        return 31 * unit.getClass().hashCode() + (int)(bits ^ (bits >>> 32));
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null.");
        }
        double baseValue      = unit.convertToBaseUnit(value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
        return new Quantity<>(convertedValue, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity must not be null.");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null.");
        }
        double thisBase  = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);
        double sumBase   = thisBase + otherBase;
        double result    = targetUnit.convertFromBaseUnit(sumBase);
        return new Quantity<>(result, targetUnit);
    }

    @Override
    public String toString() {
        return String.format("Quantity(%.6f, %s)", value, unit.getUnitName());
    }
}
