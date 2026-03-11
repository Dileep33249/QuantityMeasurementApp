package org.quantitymeasurement;
public class QuantityLength {

    private static final double EPSILON = 1e-6;

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        validateValue(value);
        validateUnit(unit);
        this.value = value;
        this.unit  = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {
        validateUnit(targetUnit);
        double converted = convertToDouble(this.value, this.unit, targetUnit);
        return new QuantityLength(converted, targetUnit);
    }

    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        validateValue(value);
        validateUnit(sourceUnit);
        validateUnit(targetUnit);
        return convertToDouble(value, sourceUnit, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;
        double thisInBase  = toBaseUnit(this.value,  this.unit);
        double otherInBase = toBaseUnit(other.value, other.unit);

        return Math.abs(thisInBase - otherInBase) < EPSILON;
    }

    @Override
    public int hashCode() {
        double baseValue = toBaseUnit(this.value, this.unit);
        long rounded = Math.round(baseValue / EPSILON);
        return Long.hashCode(rounded);
    }

    @Override
    public String toString() {
        return value + " " + unit.name();
    }

    private static double convertToDouble(double value, LengthUnit source, LengthUnit target) {
        double baseValue = toBaseUnit(value, source);
        return fromBaseUnit(baseValue, target);
    }

    private static double toBaseUnit(double value, LengthUnit unit) {
        return value * unit.getConversionFactor();
    }

    private static double fromBaseUnit(double baseValue, LengthUnit unit) {
        return baseValue / unit.getConversionFactor();
    }

    private static void validateValue(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Value must be a finite number, but was: " + value);
        }
    }

    private static void validateUnit(LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("LengthUnit must not be null.");
        }
    }
}