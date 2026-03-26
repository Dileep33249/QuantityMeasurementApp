package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.MeasurementType;
import com.app.quantitymeasurement.model.OperationType;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.UnitDefinition;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuantityMeasurementDTO compareQuantities(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return execute(OperationType.COMPARE, thisQuantity, thatQuantity, (leftBase, rightBase, leftUnit, rightUnit) ->
                resultWithString(String.valueOf(Double.compare(leftBase, rightBase) == 0)));
    }

    @Override
    public QuantityMeasurementDTO convertQuantity(QuantityDTO sourceQuantity, QuantityDTO targetQuantity) {
        return execute(OperationType.CONVERT, sourceQuantity, targetQuantity, (leftBase, ignored, leftUnit, rightUnit) -> {
            QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
            dto.setResultValue(round(rightUnit.fromBase(leftBase)));
            dto.setResultUnit(rightUnit.name());
            dto.setResultMeasurementType(rightUnit.getMeasurementType().name());
            return dto;
        });
    }

    @Override
    public QuantityMeasurementDTO addQuantities(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return execute(OperationType.ADD, thisQuantity, thatQuantity, (leftBase, rightBase, leftUnit, ignored) ->
                buildNumericResult(leftUnit, leftBase + rightBase));
    }

    @Override
    public QuantityMeasurementDTO subtractQuantities(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return execute(OperationType.SUBTRACT, thisQuantity, thatQuantity, (leftBase, rightBase, leftUnit, ignored) ->
                buildNumericResult(leftUnit, leftBase - rightBase));
    }

    @Override
    public QuantityMeasurementDTO multiplyQuantities(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return execute(OperationType.MULTIPLY, thisQuantity, thatQuantity, (leftBase, rightBase, leftUnit, ignored) ->
                buildNumericResult(leftUnit, leftBase * rightBase));
    }

    @Override
    public QuantityMeasurementDTO divideQuantities(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return execute(OperationType.DIVIDE, thisQuantity, thatQuantity, (leftBase, rightBase, leftUnit, ignored) -> {
            if (Double.compare(rightBase, 0.0) == 0) {
                throw new ArithmeticException("Divide by zero");
            }
            return buildNumericResult(leftUnit, leftBase / rightBase);
        });
    }

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByOperationIgnoreCase(operation));
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementHistory(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByThisMeasurementTypeIgnoreCase(measurementType));
    }

    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationIgnoreCaseAndErrorFalse(operation);
    }

    @Override
    public List<QuantityMeasurementDTO> getErroredHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrue());
    }

    private QuantityMeasurementDTO execute(OperationType operationType,
                                           QuantityDTO thisQuantity,
                                           QuantityDTO thatQuantity,
                                           OperationExecutor executor) {
        try {
            validateCompatibleMeasurements(operationType, thisQuantity, thatQuantity);
            UnitDefinition leftUnit = UnitDefinition.from(thisQuantity.getUnit());
            UnitDefinition rightUnit = UnitDefinition.from(thatQuantity.getUnit());
            double leftBase = leftUnit.toBase(thisQuantity.getValue());
            double rightBase = rightUnit.toBase(thatQuantity.getValue());
            QuantityMeasurementDTO result = executor.execute(leftBase, rightBase, leftUnit, rightUnit);
            return persistSuccess(operationType, thisQuantity, thatQuantity, result);
        } catch (RuntimeException ex) {
            persistError(operationType, thisQuantity, thatQuantity, ex.getMessage());
            throw ex;
        }
    }

    private void validateCompatibleMeasurements(OperationType operationType, QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        MeasurementType leftType = thisQuantity.getMeasurementType();
        MeasurementType rightType = thatQuantity.getMeasurementType();
        if (leftType != rightType) {
            throw new QuantityMeasurementException(operationType.name().toLowerCase() +
                    " Error: Cannot perform arithmetic between different measurement categories: " +
                    leftType + " and " + rightType);
        }
    }

    private QuantityMeasurementDTO buildNumericResult(UnitDefinition resultUnit, double baseValue) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setResultValue(round(resultUnit.fromBase(baseValue)));
        dto.setResultUnit(resultUnit.name());
        dto.setResultMeasurementType(resultUnit.getMeasurementType().name());
        return dto;
    }

    private QuantityMeasurementDTO persistSuccess(OperationType operationType,
                                                  QuantityDTO thisQuantity,
                                                  QuantityDTO thatQuantity,
                                                  QuantityMeasurementDTO result) {
        QuantityMeasurementEntity entity = baseEntity(operationType, thisQuantity, thatQuantity);
        entity.setResultString(result.getResultString());
        entity.setResultValue(result.getResultValue());
        entity.setResultUnit(result.getResultUnit());
        entity.setResultMeasurementType(result.getResultMeasurementType());
        entity.setError(false);
        return QuantityMeasurementDTO.fromEntity(repository.save(entity));
    }

    private void persistError(OperationType operationType, QuantityDTO thisQuantity, QuantityDTO thatQuantity, String message) {
        QuantityMeasurementEntity entity = baseEntity(operationType, thisQuantity, thatQuantity);
        entity.setError(true);
        entity.setErrorMessage(message);
        entity.setResultValue(0.0);
        repository.save(entity);
    }

    private QuantityMeasurementEntity baseEntity(OperationType operationType, QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(thisQuantity.getValue());
        entity.setThisUnit(thisQuantity.getUnit());
        entity.setThisMeasurementType(thisQuantity.getMeasurementType().name());
        entity.setThatValue(thatQuantity.getValue());
        entity.setThatUnit(thatQuantity.getUnit());
        entity.setThatMeasurementType(thatQuantity.getMeasurementType().name());
        entity.setOperation(operationType.name().toLowerCase());
        return entity;
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

    private QuantityMeasurementDTO resultWithString(String value) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setResultString(value);
        return dto;
    }

    @FunctionalInterface
    private interface OperationExecutor {
        QuantityMeasurementDTO execute(double leftBase, double rightBase, UnitDefinition leftUnit, UnitDefinition rightUnit);
    }
}
