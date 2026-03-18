package org.quantitymeasurement.unit;

public interface IMeasurable {
    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getMeasurementType();

    Enum<?> asEnum();

    boolean supportsArithmetic();

    IMeasurable getUnitByName(String unitName);
}
