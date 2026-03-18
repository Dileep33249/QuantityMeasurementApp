package org.quantitymeasurement.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * QuantityMeasurementEntity is a comprehensive data holder for all aspects of a
 * quantity
 * measurement operation, including the operands, operation type, and result.
 * This entity is designed to be immutable through constructor-based
 * initialization.
 *
 * Implements Serializable for persistence to disk storage.
 */
public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum OperationType {
        CONVERSION("Conversion"),
        COMPARISON("Comparison"),
        ADDITION("Addition"),
        SUBTRACTION("Subtraction"),
        DIVISION("Division");

        private final String displayName;

        OperationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Operation details
    private String operationType;
    private String operationResult;

    // Operand 1 (single or first operand)
    private double operand1Value;
    private String operand1Unit;
    private String operand1MeasurementType;

    // Operand 2 (binary operations)
    private Double operand2Value;
    private String operand2Unit;
    private String operand2MeasurementType;

    // Result
    private Double resultValue;
    private String resultUnit;
    private String resultMeasurementType;

    // Error handling
    private boolean hasError;
    private String errorMessage;

    /**
     * Constructor for single-operand operations (e.g., conversion).
     */
    public QuantityMeasurementEntity(
            OperationType operationType,
            double operand1Value,
            String operand1Unit,
            String operand1MeasurementType,
            Double resultValue,
            String resultUnit,
            String resultMeasurementType) {
        this.operationType = operationType.getDisplayName();
        this.operationResult = (resultValue != null) ? resultValue.toString() : null;
        this.operand1Value = operand1Value;
        this.operand1Unit = operand1Unit;
        this.operand1MeasurementType = operand1MeasurementType;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.resultMeasurementType = resultMeasurementType;
        this.hasError = false;
        this.errorMessage = null;
    }

    /**
     * Constructor for binary operations (addition, subtraction, division).
     */
    public QuantityMeasurementEntity(
            OperationType operationType,
            double operand1Value,
            String operand1Unit,
            String operand1MeasurementType,
            double operand2Value,
            String operand2Unit,
            String operand2MeasurementType,
            Double resultValue,
            String resultUnit,
            String resultMeasurementType) {
        this.operationType = operationType.getDisplayName();
        this.operationResult = (resultValue != null) ? resultValue.toString() : null;
        this.operand1Value = operand1Value;
        this.operand1Unit = operand1Unit;
        this.operand1MeasurementType = operand1MeasurementType;
        this.operand2Value = operand2Value;
        this.operand2Unit = operand2Unit;
        this.operand2MeasurementType = operand2MeasurementType;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.resultMeasurementType = resultMeasurementType;
        this.hasError = false;
        this.errorMessage = null;
    }

    /**
     * Constructor for error scenarios with binary operands.
     */
    public QuantityMeasurementEntity(
            OperationType operationType,
            double operand1Value,
            String operand1Unit,
            String operand1MeasurementType,
            double operand2Value,
            String operand2Unit,
            String operand2MeasurementType,
            String errorMessage) {
        this.operationType = operationType.getDisplayName();
        this.operand1Value = operand1Value;
        this.operand1Unit = operand1Unit;
        this.operand1MeasurementType = operand1MeasurementType;
        this.operand2Value = operand2Value;
        this.operand2Unit = operand2Unit;
        this.operand2MeasurementType = operand2MeasurementType;
        this.hasError = true;
        this.errorMessage = errorMessage;
        this.operationResult = "ERROR";
    }

    /**
     * Constructor for error scenarios with single operand.
     */
    public QuantityMeasurementEntity(
            OperationType operationType,
            double operand1Value,
            String operand1Unit,
            String operand1MeasurementType,
            String errorMessage) {
        this.operationType = operationType.getDisplayName();
        this.operand1Value = operand1Value;
        this.operand1Unit = operand1Unit;
        this.operand1MeasurementType = operand1MeasurementType;
        this.hasError = true;
        this.errorMessage = errorMessage;
        this.operationResult = "ERROR";
    }

    // Getters
    public String getOperationType() {
        return operationType;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public double getOperand1Value() {
        return operand1Value;
    }

    public String getOperand1Unit() {
        return operand1Unit;
    }

    public String getOperand1MeasurementType() {
        return operand1MeasurementType;
    }

    public Double getOperand2Value() {
        return operand2Value;
    }

    public String getOperand2Unit() {
        return operand2Unit;
    }

    public String getOperand2MeasurementType() {
        return operand2MeasurementType;
    }

    public Double getResultValue() {
        return resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public String getResultMeasurementType() {
        return resultMeasurementType;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof QuantityMeasurementEntity))
            return false;
        QuantityMeasurementEntity that = (QuantityMeasurementEntity) o;
        return Double.compare(that.operand1Value, operand1Value) == 0
                && hasError == that.hasError
                && Objects.equals(operationType, that.operationType)
                && Objects.equals(operand1Unit, that.operand1Unit)
                && Objects.equals(operand1MeasurementType, that.operand1MeasurementType)
                && Objects.equals(operand2Value, that.operand2Value)
                && Objects.equals(operand2Unit, that.operand2Unit)
                && Objects.equals(operand2MeasurementType, that.operand2MeasurementType)
                && Objects.equals(resultValue, that.resultValue)
                && Objects.equals(resultUnit, that.resultUnit)
                && Objects.equals(resultMeasurementType, that.resultMeasurementType)
                && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                operationType, operand1Value, operand1Unit, operand1MeasurementType,
                operand2Value, operand2Unit, operand2MeasurementType,
                resultValue, resultUnit, resultMeasurementType, hasError, errorMessage);
    }

    @Override
    public String toString() {
        if (hasError) {
            return "QuantityMeasurementEntity [Operation: " + operationType
                    + ", Operand1: " + operand1Value + " " + operand1Unit
                    + (operand2Value != null ? ", Operand2: " + operand2Value + " " + operand2Unit : "")
                    + ", ERROR: " + errorMessage + "]";
        }
        return "QuantityMeasurementEntity [Operation: " + operationType
                + ", Operand1: " + operand1Value + " " + operand1Unit
                + (operand2Value != null ? ", Operand2: " + operand2Value + " " + operand2Unit : "")
                + ", Result: " + resultValue + " " + resultUnit + "]";
    }
}
