package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.Quantity;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
        LOGGER.info("QuantityMeasurementServiceImpl initialized with {}", repository.getClass().getSimpleName());
    }

    @Override
    public QuantityMeasurementEntity add(QuantityDTO quantityOne, QuantityDTO quantityTwo) {
        return executeOperation("ADD", quantityOne.getMeasurementType(), () -> {
            Quantity<IMeasurable> first = createQuantity(quantityOne);
            Quantity<IMeasurable> second = createQuantity(quantityTwo);
            first.getUnit().validOperationSupport("ADD");
            return first.add(second).toString();
        });
    }

    @Override
    public QuantityMeasurementEntity subtract(QuantityDTO quantityOne, QuantityDTO quantityTwo) {
        return executeOperation("SUBTRACT", quantityOne.getMeasurementType(), () -> {
            Quantity<IMeasurable> first = createQuantity(quantityOne);
            Quantity<IMeasurable> second = createQuantity(quantityTwo);
            first.getUnit().validOperationSupport("SUBTRACT");
            return first.subtract(second).toString();
        });
    }

    @Override
    public QuantityMeasurementEntity divide(QuantityDTO quantityOne, QuantityDTO quantityTwo) {
        return executeOperation("DIVIDE", quantityOne.getMeasurementType(), () -> {
            Quantity<IMeasurable> first = createQuantity(quantityOne);
            Quantity<IMeasurable> second = createQuantity(quantityTwo);
            first.getUnit().validOperationSupport("DIVIDE");
            return String.valueOf(first.divide(second));
        });
    }

    @Override
    public QuantityMeasurementEntity convert(QuantityDTO quantity, String targetUnit) {
        return executeOperation("CONVERT", quantity.getMeasurementType(), () -> {
            Quantity<IMeasurable> sourceQuantity = createQuantity(quantity);
            IMeasurable target = resolveUnit(new QuantityDTO(0, targetUnit, quantity.getMeasurementType()));
            return sourceQuantity.convertTo(target).toString();
        });
    }

    @Override
    public QuantityMeasurementEntity compare(QuantityDTO quantityOne, QuantityDTO quantityTwo) {
        return executeOperation("COMPARE", quantityOne.getMeasurementType(), () -> {
            Quantity<IMeasurable> first = createQuantity(quantityOne);
            Quantity<IMeasurable> second = createQuantity(quantityTwo);
            if (!first.getUnit().getClass().equals(second.getUnit().getClass())) {
                throw new IllegalArgumentException("Cross category comparison not allowed");
            }
            return String.valueOf(first.equals(second));
        });
    }

    private QuantityMeasurementEntity executeOperation(String operation, String measurementType, Supplier<String> action) {
        try {
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(operation, action.get(), measurementType);
            repository.save(entity);
            LOGGER.info("{} operation completed: {}", operation, entity);
            return entity;
        } catch (Exception exception) {
            LOGGER.error("{} operation failed: {}", operation, exception.getMessage());
            return new QuantityMeasurementEntity(exception.getMessage());
        }
    }

    private Quantity<IMeasurable> createQuantity(QuantityDTO quantity) {
        return new Quantity<>(quantity.getValue(), resolveUnit(quantity));
    }

    private IMeasurable resolveUnit(QuantityDTO quantity) {
        return switch (quantity.getMeasurementType().toUpperCase()) {
            case "LENGTH" -> LengthUnit.valueOf(quantity.getUnit().toUpperCase());
            case "WEIGHT" -> WeightUnit.valueOf(quantity.getUnit().toUpperCase());
            case "VOLUME" -> VolumeUnit.valueOf(quantity.getUnit().toUpperCase());
            case "TEMPERATURE" -> TemperatureUnit.valueOf(quantity.getUnit().toUpperCase());
            default -> throw new IllegalArgumentException("Invalid measurement type: " + quantity.getMeasurementType());
        };
    }
}
