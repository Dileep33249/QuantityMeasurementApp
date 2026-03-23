package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityMeasurementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementController.class);

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
        LOGGER.info("QuantityMeasurementController initialized");
    }

    public QuantityMeasurementEntity performAddition(QuantityDTO firstQuantity, QuantityDTO secondQuantity) {
        QuantityMeasurementEntity measurement = service.add(firstQuantity, secondQuantity);
        LOGGER.info("Addition request completed: {}", measurement);
        return measurement;
    }

    public QuantityMeasurementEntity performSubtraction(QuantityDTO firstQuantity, QuantityDTO secondQuantity) {
        QuantityMeasurementEntity measurement = service.subtract(firstQuantity, secondQuantity);
        LOGGER.info("Subtraction request completed: {}", measurement);
        return measurement;
    }

    public QuantityMeasurementEntity performDivision(QuantityDTO firstQuantity, QuantityDTO secondQuantity) {
        QuantityMeasurementEntity measurement = service.divide(firstQuantity, secondQuantity);
        LOGGER.info("Division request completed: {}", measurement);
        return measurement;
    }

    public QuantityMeasurementEntity performConversion(QuantityDTO quantity, String targetUnit) {
        QuantityMeasurementEntity measurement = service.convert(quantity, targetUnit);
        LOGGER.info("Conversion request completed: {}", measurement);
        return measurement;
    }

    public QuantityMeasurementEntity performComparison(QuantityDTO firstQuantity, QuantityDTO secondQuantity) {
        QuantityMeasurementEntity measurement = service.compare(firstQuantity, secondQuantity);
        LOGGER.info("Comparison request completed: {}", measurement);
        return measurement;
    }
}
