package com.app.quantitymeasurement.unit;

public interface IMeasurable {

    double getConversionValue();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();

    String getMeasurementType();

    IMeasurable getUnitInstance(String unitName);

    default boolean supportArithmetic() {
        return true;
    }

    default void validOperationSupport(String operation) {
        if (!supportArithmetic()) {
            throw new UnsupportedOperationException(getUnitName() + " does not support " + operation + " operations.");
        }
    }
}
