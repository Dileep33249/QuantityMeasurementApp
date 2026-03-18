package org.quantitymeasurement.unit;

/**
 * WeightUnit is an enum that represents different units of weight/mass
 * measurement.
 * It implements IMeasurable to provide conversion capabilities between
 * different weight units.
 */
public enum WeightUnit implements IMeasurable {
    GRAM(0.001),
    KILOGRAM(1.0),
    TONNE(1000.0),
    POUND(0.453592),
    OUNCE(0.0283495);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
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
        return "WEIGHT";
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
            return WeightUnit.valueOf(unitName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
