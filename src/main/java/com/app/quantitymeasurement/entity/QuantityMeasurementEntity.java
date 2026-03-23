package com.app.quantitymeasurement.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class QuantityMeasurementEntity implements Serializable {

    private Long id;
    private final String operation;
    private final String result;
    private String measurementType;
    private final boolean error;
    private LocalDateTime createdAt;

    public QuantityMeasurementEntity(String operation, String result) {
        this(operation, result, null);
    }

    public QuantityMeasurementEntity(String operation, String result, String measurementType) {
        this.operation = operation;
        this.result = result;
        this.measurementType = measurementType;
        this.error = false;
        this.createdAt = LocalDateTime.now();
    }

    public QuantityMeasurementEntity(String errorMessage) {
        this.operation = "ERROR";
        this.result = errorMessage;
        this.error = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public String getResult() {
        return result;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public boolean hasError() {
        return error;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return operation + " : " + result;
    }
}
