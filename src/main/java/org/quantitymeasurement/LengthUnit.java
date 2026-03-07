package org.quantitymeasurement;

public enum LengthUnit {

    INCHES(1.0),
    FEET(12.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private final double conversionFactorToInches;

    LengthUnit(double conversionFactorToInches) {
        this.conversionFactorToInches = conversionFactorToInches;
    }

    public double getConversionFactorToInches() {
        return conversionFactorToInches;
    }
}