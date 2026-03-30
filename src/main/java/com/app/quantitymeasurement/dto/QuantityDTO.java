package com.app.quantitymeasurement.dto;

import com.app.quantitymeasurement.model.MeasurementType;
import com.app.quantitymeasurement.model.UnitDefinition;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuantityDTO {

    @NotNull(message = "Value is required")
    private Double value;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Measurement type is required")
    private MeasurementType measurementType;

    public QuantityDTO() {
    }

    public QuantityDTO(Double value, String unit, MeasurementType measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isUnitValidForMeasurementType() {
        if (unit == null || unit.isBlank() || measurementType == null) {
            return true;
        }
        try {
            return UnitDefinition.from(unit).matches(measurementType);
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
