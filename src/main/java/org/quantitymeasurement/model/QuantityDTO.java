package org.quantitymeasurement.model;

import java.util.Objects;

/**
 * Data Transfer Object (POJO) for holding quantity measurement input data.
 * Represents a quantity with a value and corresponding unit for transfer
 * between layers.
 */
public class QuantityDTO {

    private final double value;
    private final String unit;
    private final String measurementType;

    /**
     * Constructor for QuantityDTO.
     *
     * @param value           the numerical value of the quantity
     * @param unit            the unit type as a string (e.g., "METRES", "FEET")
     * @param measurementType the measurement category (e.g., "LENGTH", "WEIGHT")
     */
    public QuantityDTO(double value, String unit, String measurementType) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Quantity value must be a finite number, but received: " + value);
        }
        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit must not be null or empty.");
        }
        if (measurementType == null || measurementType.isEmpty()) {
            throw new IllegalArgumentException("MeasurementType must not be null or empty.");
        }
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof QuantityDTO))
            return false;
        QuantityDTO that = (QuantityDTO) o;
        return Double.compare(that.value, value) == 0
                && Objects.equals(unit, that.unit)
                && Objects.equals(measurementType, that.measurementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit, measurementType);
    }

    @Override
    public String toString() {
        return "QuantityDTO(" + value + ", " + unit + ", " + measurementType + ")";
    }
}
