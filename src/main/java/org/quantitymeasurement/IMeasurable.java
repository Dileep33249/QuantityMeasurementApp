package org.quantitymeasurement;
public interface IMeasurable {

    @FunctionalInterface
    interface SupportsArithmetic {
        boolean isSupported();
    }

    SupportsArithmetic supportsArithmetic = () -> true;

    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double value);

    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    default void validateOperationSupport(String operation) {
    }
}