package org.quantitymeasurement;
public enum LengthUnit implements IMeasurable {

    FEET(1.0, "Feet"),
    INCH(1.0 / 12.0, "Inch"),
    YARD(3.0, "Yard"),
    CENTIMETRE(1.0 / 30.48, "Centimetre"),
    METRE(3.28084, "Metre");

    private final double conversionFactor;
    private final String unitName;

    LengthUnit(double conversionFactor, String unitName) {
        this.conversionFactor = conversionFactor;
        this.unitName = unitName;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
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
    public String getUnitName() {
        return unitName;
    }
}