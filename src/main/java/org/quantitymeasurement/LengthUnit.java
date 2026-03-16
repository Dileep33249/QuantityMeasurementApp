package org.quantitymeasurement;
public enum LengthUnit implements IMeasurable {

    INCHES {
        @Override public double convertToBaseUnit(double v)   { return v * 0.0254; }
        @Override public double convertFromBaseUnit(double v) { return v / 0.0254; }
    },
    FEET {
        @Override public double convertToBaseUnit(double v)   { return v * 0.3048; }
        @Override public double convertFromBaseUnit(double v) { return v / 0.3048; }
    },
    METRES {
        @Override public double convertToBaseUnit(double v)   { return v; }
        @Override public double convertFromBaseUnit(double v) { return v; }
    },
    KILOMETRES {
        @Override public double convertToBaseUnit(double v)   { return v * 1000.0; }
        @Override public double convertFromBaseUnit(double v) { return v / 1000.0; }
    };
}