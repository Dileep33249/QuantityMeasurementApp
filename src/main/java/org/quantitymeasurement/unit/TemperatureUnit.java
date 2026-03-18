package org.quantitymeasurement.unit;
/**
 * TemperatureUnit is an enum that represents different units of temperature
 * measurement.
 * It implements IMeasurable to provide conversion capabilities between
 * different temperature units.
 * Note: Temperature units do NOT support arithmetic operations (add, subtract,
 * divide).
 */
public enum TemperatureUnit implements IMeasurable {
    CELSIUS(0.0, 1.0),
    FAHRENHEIT(32.0, 5.0 / 9.0),
    KELVIN(-273.15, 1.0);

    private final double offset;
    private final double factor;

    TemperatureUnit(double offset, double factor) {
        this.offset = offset;
        this.factor = factor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        // Convert to Kelvin (base unit for temperature)
        if (this == CELSIUS) {
            return value + 273.15;
        } else if (this == FAHRENHEIT) {
            return (value - 32) * (5.0 / 9.0) + 273.15;
        }
        return value;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        // Convert from Kelvin to this unit
        if (this == CELSIUS) {
            return baseValue - 273.15;
        } else if (this == FAHRENHEIT) {
            return (baseValue - 273.15) * (9.0 / 5.0) + 32;
        }
        return baseValue;
    }

    @Override
    public String getMeasurementType() {
        return "TEMPERATURE";
    }

    @Override
    public Enum<?> asEnum() {
        return this;
    }

    @Override
    public boolean supportsArithmetic() {
        return false; // Temperature does not support arithmetic operations
    }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        try {
            return TemperatureUnit.valueOf(unitName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
