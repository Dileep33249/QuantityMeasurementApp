package org.quantitymeasurement.model;

import org.quantitymeasurement.unit.IMeasurable;
import java.util.Objects;

/**
 * Generic POJO model class for representing a quantity with its associated unit
 * of measurement.
 * Used internally within the service layer for performing operations on
 * quantities.
 *
 * @param <U> the unit type, must extend Enum and implement IMeasurable
 */
public class QuantityModel<U extends Enum<U> & IMeasurable> {

    private final double value;
    private final U unit;

    /**
     * Constructor for QuantityModel.
     *
     * @param value the numerical value of the quantity
     * @param unit  the unit of measurement
     */
    public QuantityModel(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit must not be null.");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Quantity value must be a finite number, but received: " + value);
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

    public String getMeasurementType() {
        return unit.getMeasurementType();
    }

    /**
     * Convert this quantity to another unit of the same category.
     */
    public QuantityModel<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null.");
        }
        if (!unit.getMeasurementType().equals(targetUnit.getMeasurementType())) {
            throw new IllegalArgumentException(
                    "Cannot convert between different unit categories");
        }
        double baseValue = unit.convertToBaseUnit(value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
        return new QuantityModel<>(convertedValue, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof QuantityModel))
            return false;
        QuantityModel<?> that = (QuantityModel<?>) o;
        return Double.compare(that.value, value) == 0 && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "QuantityModel(" + value + ", " + unit + ")";
    }
}
