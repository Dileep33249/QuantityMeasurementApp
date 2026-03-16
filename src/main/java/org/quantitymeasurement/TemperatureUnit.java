package org.quantitymeasurement;
import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {

    CELSIUS {
        final Function<Double, Double> CELSIUS_TO_CELSIUS = (celsius) -> celsius;

        @Override
        public double convertToBaseUnit(double value) {
            return CELSIUS_TO_CELSIUS.apply(value);
        }

        @Override
        public double convertFromBaseUnit(double celsius) {
            return CELSIUS_TO_CELSIUS.apply(celsius);
        }
    },

    FAHRENHEIT {
        final Function<Double, Double> FAHRENHEIT_TO_CELSIUS = (f) -> (f - 32.0) * 5.0 / 9.0;
        final Function<Double, Double> CELSIUS_TO_FAHRENHEIT = (c) -> (c * 9.0 / 5.0) + 32.0;

        @Override
        public double convertToBaseUnit(double value) {
            return FAHRENHEIT_TO_CELSIUS.apply(value);
        }

        @Override
        public double convertFromBaseUnit(double celsius) {
            return CELSIUS_TO_FAHRENHEIT.apply(celsius);
        }
    },

    KELVIN {
        final Function<Double, Double> KELVIN_TO_CELSIUS = (k) -> k - 273.15;
        final Function<Double, Double> CELSIUS_TO_KELVIN = (c) -> c + 273.15;

        @Override
        public double convertToBaseUnit(double value) {
            return KELVIN_TO_CELSIUS.apply(value);
        }

        @Override
        public double convertFromBaseUnit(double celsius) {
            return CELSIUS_TO_KELVIN.apply(celsius);
        }
    };

    SupportsArithmetic supportsArithmetic = () -> false;

    @Override
    public boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException(
                "Temperature does not support " + operation
                        + ". Temperature values represent points on a scale; "
                        + "arithmetic operations are not meaningful for absolute temperatures.");
    }
}
