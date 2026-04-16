package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.MeasurementType;
import com.app.quantitymeasurement.model.OperationType;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.UnitDefinition;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return execute(OperationType.COMPARE, thisQuantity, thatQuantity, (leftBase, rightBase, leftUnit, rightUnit) -> {
            boolean equal = nearlyEqual(leftBase, rightBase);
            return resultWithString(String.valueOf(equal));
        });
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
        return execute(OperationType.MULTIPLY, thisQuantity, thatQuantity, (leftBase, rightBase, leftUnit, ignored) -> {
            double leftValue = leftUnit.fromBase(leftBase);
            double rightValue = leftUnit.fromBase(rightBase);
            double result = leftValue * rightValue;
            return buildNumericResult(leftUnit, leftUnit.toBase(result));
        });
    }

    @Override
    public QuantityMeasurementDTO divideQuantities(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return execute(OperationType.DIVIDE, thisQuantity, thatQuantity, (leftBase, rightBase, leftUnit, ignored) -> {
            double leftValue = leftUnit.fromBase(leftBase);
            double rightValue = leftUnit.fromBase(rightBase);
            if (Double.compare(rightValue, 0.0) == 0) {
                throw new ArithmeticException("Divide by zero");
            }
            double result = leftValue / rightValue;
            return buildNumericResult(leftUnit, leftUnit.toBase(result));
        });
    }

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByCreatedByUsernameIgnoreCaseAndOperationIgnoreCase(currentUsername(), operation)
        );
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementHistory(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByCreatedByUsernameIgnoreCaseAndThisMeasurementTypeIgnoreCase(currentUsername(), measurementType)
        );
    }

    @Override
    public long getOperationCount(String operation) {
        return repository.countByCreatedByUsernameIgnoreCaseAndOperationIgnoreCaseAndErrorFalse(currentUsername(), operation);
    }

    @Override
    public List<QuantityMeasurementDTO> getErroredHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findErroredByCreatedByUsername(currentUsername()));
    }

    private QuantityMeasurementDTO execute(OperationType operationType,
                                           QuantityDTO thisQuantity,
                                           QuantityDTO thatQuantity,
                                           OperationExecutor executor) {
        boolean authenticated = isAuthenticated();
        try {
            validateCompatibleMeasurements(operationType, thisQuantity, thatQuantity);
            UnitDefinition leftUnit = UnitDefinition.from(thisQuantity.getUnit());
            UnitDefinition rightUnit = UnitDefinition.from(thatQuantity.getUnit());
            double leftBase = leftUnit.toBase(thisQuantity.getValue());
            double rightBase = rightUnit.toBase(thatQuantity.getValue());
            QuantityMeasurementDTO result = executor.execute(leftBase, rightBase, leftUnit, rightUnit);
            result.setOperation(operationType.name().toLowerCase());
            if (authenticated) {
                return persistSuccess(operationType, thisQuantity, thatQuantity, result);
            }
            return result;
        } catch (RuntimeException ex) {
            if (authenticated) {
                persistError(operationType, thisQuantity, thatQuantity, ex.getMessage());
            }
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
        entity.setCreatedByUsername(currentUsername());
        return entity;
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new QuantityMeasurementException("Authenticated user not found");
        }
        return authentication.getName();
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

    private QuantityMeasurementDTO resultWithString(String value) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setResultString(value);
        return dto;
    }

    private boolean nearlyEqual(double left, double right) {
        double diff = Math.abs(left - right);
        double scale = Math.max(1.0, Math.max(Math.abs(left), Math.abs(right)));
        return diff <= 1e-6 * scale;
    }

    @FunctionalInterface
    private interface OperationExecutor {
        QuantityMeasurementDTO execute(double leftBase, double rightBase, UnitDefinition leftUnit, UnitDefinition rightUnit);
    }
}
