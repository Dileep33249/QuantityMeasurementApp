package com.app.quantitymeasurement.unit;

public enum WeightUnit implements IMeasurable {
    MILLIGRAM(0.001), GRAM(1.0), KILOGRAM(1000.0), POUND(453.592), TONNE(1000000.0);

    private final double conversionValue;

    WeightUnit(double conversionValue) {
        this.conversionValue = conversionValue;
    }

    @Override
    public double getConversionValue() {
        return conversionValue;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionValue;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionValue;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return getClass().getSimpleName();
    }

    @Override
    public IMeasurable getUnitInstance(String unitName) {
        for (WeightUnit unit : values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid weight unit: " + unitName);
    }
}
