package org.quantitymeasurement.unit;

/**
 * LengthUnit is an enum that represents different units of length measurement.
 * It implements IMeasurable to provide conversion capabilities between
 * different length units.
 */
public enum LengthUnit implements IMeasurable {
    FEET(0.3048),
    INCHES(0.0254),
    METRES(1.0),
    KILOMETRES(1000.0),
    CENTIMETERS(0.01),
    YARDS(0.9144);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getMeasurementType() {
        return "LENGTH";
    }

    @Override
    public Enum<?> asEnum() {
        return this;
    }

    @Override
    public boolean supportsArithmetic() {
        return true;
    }

    @Override
    public IMeasurable getUnitByName(String unitName) {
        try {
            return LengthUnit.valueOf(unitName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
