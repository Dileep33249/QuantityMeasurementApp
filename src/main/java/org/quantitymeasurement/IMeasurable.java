package org.quantitymeasurement;
public interface IMeasurable {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double value);
}