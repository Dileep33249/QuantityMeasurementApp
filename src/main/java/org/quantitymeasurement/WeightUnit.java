package org.quantitymeasurement;
public enum WeightUnit implements IMeasurable {

    GRAM {
        @Override public double convertToBaseUnit(double v)   { return v; }
        @Override public double convertFromBaseUnit(double v) { return v; }
    },
    KILOGRAM {
        @Override public double convertToBaseUnit(double v)   { return v * 1000.0; }
        @Override public double convertFromBaseUnit(double v) { return v / 1000.0; }
    },
    TONNE {
        @Override public double convertToBaseUnit(double v)   { return v * 1_000_000.0; }
        @Override public double convertFromBaseUnit(double v) { return v / 1_000_000.0; }
    };
}