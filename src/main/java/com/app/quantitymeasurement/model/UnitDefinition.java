package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum UnitDefinition {
    FEET(MeasurementType.LengthUnit, 12.0, 0.0),
    INCHES(MeasurementType.LengthUnit, 1.0, 0.0),
    YARD(MeasurementType.LengthUnit, 36.0, 0.0),
    CENTIMETER(MeasurementType.LengthUnit, 0.3937007874, 0.0),

    GALLON(MeasurementType.VolumeUnit, 3.78541, 0.0),
    LITER(MeasurementType.VolumeUnit, 1.0, 0.0),
    MILLILITER(MeasurementType.VolumeUnit, 0.001, 0.0),

    KILOGRAM(MeasurementType.WeightUnit, 1000.0, 0.0),
    GRAM(MeasurementType.WeightUnit, 1.0, 0.0),
    TONNE(MeasurementType.WeightUnit, 1_000_000.0, 0.0),

    CELSIUS(MeasurementType.TemperatureUnit, 1.0, 0.0),
    FAHRENHEIT(MeasurementType.TemperatureUnit, 5.0 / 9.0, -32.0),
    KELVIN(MeasurementType.TemperatureUnit, 1.0, -273.15);

    private static final Map<String, UnitDefinition> BY_NAME = Arrays.stream(values())
            .collect(Collectors.toMap(UnitDefinition::name, Function.identity()));

    private final MeasurementType measurementType;
    private final double baseFactor;
    private final double baseOffset;

    UnitDefinition(MeasurementType measurementType, double baseFactor, double baseOffset) {
        this.measurementType = measurementType;
        this.baseFactor = baseFactor;
        this.baseOffset = baseOffset;
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public static UnitDefinition from(String unitName) {
        UnitDefinition definition = BY_NAME.get(unitName.toUpperCase());
        if (definition == null) {
            throw new QuantityMeasurementException("Invalid unit name: " + unitName + ".");
        }
        return definition;
    }

    public boolean matches(MeasurementType type) {
        return measurementType == type;
    }

    public double toBase(double value) {
        if (measurementType == MeasurementType.TemperatureUnit) {
            return (value + baseOffset) * baseFactor;
        }
        return value * baseFactor;
    }

    public double fromBase(double baseValue) {
        if (measurementType == MeasurementType.TemperatureUnit) {
            return (baseValue / baseFactor) - baseOffset;
        }
        return baseValue / baseFactor;
    }
}
