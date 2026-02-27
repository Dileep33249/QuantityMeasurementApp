package org.quantitymeasurement;

public enum LengthUnit {

    FEET(1.0),
    INCH(1.0 / 12.0);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double toFeet(double value) {
        return value * conversionFactor;
    }
}
