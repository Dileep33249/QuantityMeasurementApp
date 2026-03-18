package org.quantitymeasurement.unit;
/**
 * VolumeUnit is an enum that represents different units of volume measurement.
 * It implements IMeasurable to provide conversion capabilities between
 * different volume units.
 */
public enum VolumeUnit implements IMeasurable {
    MILLILITRE(0.001),
    LITRE(1.0),
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
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
        return "VOLUME";
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
            return VolumeUnit.valueOf(unitName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
