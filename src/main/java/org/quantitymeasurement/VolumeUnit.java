package org.quantitymeasurement;
public enum VolumeUnit implements IMeasurable {

    MILLILITRE {
        @Override public double convertToBaseUnit(double v)   { return v; }
        @Override public double convertFromBaseUnit(double v) { return v; }
    },
    LITRE {
        @Override public double convertToBaseUnit(double v)   { return v * 1000.0; }
        @Override public double convertFromBaseUnit(double v) { return v / 1000.0; }
    },
    GALLON {
        @Override public double convertToBaseUnit(double v)   { return v * 3785.41; }
        @Override public double convertFromBaseUnit(double v) { return v / 3785.41; }
    };
}
