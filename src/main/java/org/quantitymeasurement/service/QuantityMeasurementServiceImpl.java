package org.quantitymeasurement.service;

import org.quantitymeasurement.unit.IMeasurable;
import org.quantitymeasurement.unit.LengthUnit;
import org.quantitymeasurement.unit.WeightUnit;
import org.quantitymeasurement.unit.VolumeUnit;
import org.quantitymeasurement.unit.TemperatureUnit;
import org.quantitymeasurement.model.QuantityDTO;
import org.quantitymeasurement.model.QuantityMeasurementEntity;
import org.quantitymeasurement.repository.IQuantityMeasurementRepository;
import org.quantitymeasurement.exception.QuantityMeasurementException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository must not be null.");
        }
        this.repository = repository;
    }

    /**
     * Convert a quantity from one unit to another.
     *
     * @param quantityDTO the quantity to convert
     * @param targetUnit  the target unit for conversion
     * @return a QuantityMeasurementEntity containing the conversion result or error
     */
    @Override
    public QuantityMeasurementEntity convert(QuantityDTO quantityDTO, String targetUnit) {
        try {
            if (quantityDTO == null) {
                throw new IllegalArgumentException("QuantityDTO must not be null.");
            }
            if (targetUnit == null || targetUnit.isEmpty()) {
                throw new IllegalArgumentException("Target unit must not be null or empty.");
            }

            String measurementType = quantityDTO.getMeasurementType();
            IMeasurable sourceUnit = getUnitByName(quantityDTO.getUnit(), measurementType);
            IMeasurable targetUnitObj = getUnitByName(targetUnit, measurementType);

            if (sourceUnit == null) {
                throw new IllegalArgumentException(
                        "Unknown unit: " + quantityDTO.getUnit() + " for type: " + measurementType);
            }
            if (targetUnitObj == null) {
                throw new IllegalArgumentException(
                        "Unknown unit: " + targetUnit + " for type: " + measurementType);
            }

            double sourceBaseValue = sourceUnit.convertToBaseUnit(quantityDTO.getValue());
            double targetValue = targetUnitObj.convertFromBaseUnit(sourceBaseValue);
            targetValue = roundToTwoDecimals(targetValue);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.CONVERSION,
                    quantityDTO.getValue(),
                    quantityDTO.getUnit(),
                    measurementType,
                    targetValue,
                    targetUnit,
                    measurementType);
            repository.save(entity);
            return entity;

        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.CONVERSION,
                    quantityDTO != null ? quantityDTO.getValue() : 0.0,
                    quantityDTO != null ? quantityDTO.getUnit() : "UNKNOWN",
                    quantityDTO != null ? quantityDTO.getMeasurementType() : "UNKNOWN",
                    e.getMessage());
            repository.save(errorEntity);
            return errorEntity;
        }
    }

    /**
     * Compare two quantities for equality.
     *
     * @param quantity1DTO the first quantity
     * @param quantity2DTO the second quantity
     * @return a QuantityMeasurementEntity indicating if quantities are equal
     */
    @Override
    public QuantityMeasurementEntity compare(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO) {
        try {
            if (quantity1DTO == null || quantity2DTO == null) {
                throw new IllegalArgumentException("Both quantities must not be null.");
            }

            if (!quantity1DTO.getMeasurementType().equals(quantity2DTO.getMeasurementType())) {
                throw new IllegalArgumentException(
                        "Cannot compare quantities of different types: "
                                + quantity1DTO.getMeasurementType()
                                + " and "
                                + quantity2DTO.getMeasurementType());
            }

            IMeasurable unit1 = getUnitByName(quantity1DTO.getUnit(), quantity1DTO.getMeasurementType());
            IMeasurable unit2 = getUnitByName(quantity2DTO.getUnit(), quantity2DTO.getMeasurementType());

            if (unit1 == null) {
                throw new IllegalArgumentException("Unknown unit: " + quantity1DTO.getUnit());
            }
            if (unit2 == null) {
                throw new IllegalArgumentException("Unknown unit: " + quantity2DTO.getUnit());
            }

            double value1Base = unit1.convertToBaseUnit(quantity1DTO.getValue());
            double value2Base = unit2.convertToBaseUnit(quantity2DTO.getValue());
            // Use tolerance for floating point comparison (1e-9)
            boolean areEqual = Math.abs(value1Base - value2Base) < 1e-9;

            double resultValue = areEqual ? 1.0 : 0.0;
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.COMPARISON,
                    quantity1DTO.getValue(),
                    quantity1DTO.getUnit(),
                    quantity1DTO.getMeasurementType(),
                    quantity2DTO.getValue(),
                    quantity2DTO.getUnit(),
                    quantity2DTO.getMeasurementType(),
                    resultValue,
                    "boolean",
                    "N/A");
            repository.save(entity);
            return entity;

        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.COMPARISON,
                    quantity1DTO != null ? quantity1DTO.getValue() : 0.0,
                    quantity1DTO != null ? quantity1DTO.getUnit() : "UNKNOWN",
                    quantity1DTO != null ? quantity1DTO.getMeasurementType() : "UNKNOWN",
                    quantity2DTO != null ? quantity2DTO.getValue() : 0.0,
                    quantity2DTO != null ? quantity2DTO.getUnit() : "UNKNOWN",
                    quantity2DTO != null ? quantity2DTO.getMeasurementType() : "UNKNOWN",
                    e.getMessage());
            repository.save(errorEntity);
            return errorEntity;
        }
    }

    /**
     * Add two quantities together.
     *
     * @param quantity1DTO the first quantity
     * @param quantity2DTO the second quantity
     * @return a QuantityMeasurementEntity containing the sum or error
     */
    @Override
    public QuantityMeasurementEntity add(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO) {
        try {
            validateArithmeticOperands(quantity1DTO, quantity2DTO);

            IMeasurable unit1 = getUnitByName(quantity1DTO.getUnit(), quantity1DTO.getMeasurementType());
            IMeasurable unit2 = getUnitByName(quantity2DTO.getUnit(), quantity2DTO.getMeasurementType());

            if (!unit1.supportsArithmetic()) {
                throw new QuantityMeasurementException(
                        "Arithmetic operations are not supported for " + quantity1DTO.getMeasurementType());
            }

            double baseValue1 = unit1.convertToBaseUnit(quantity1DTO.getValue());
            double baseValue2 = unit2.convertToBaseUnit(quantity2DTO.getValue());
            double resultBase = baseValue1 + baseValue2;

            double resultValue = unit1.convertFromBaseUnit(resultBase);
            resultValue = roundToTwoDecimals(resultValue);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.ADDITION,
                    quantity1DTO.getValue(),
                    quantity1DTO.getUnit(),
                    quantity1DTO.getMeasurementType(),
                    quantity2DTO.getValue(),
                    quantity2DTO.getUnit(),
                    quantity2DTO.getMeasurementType(),
                    resultValue,
                    quantity1DTO.getUnit(),
                    quantity1DTO.getMeasurementType());
            repository.save(entity);
            return entity;

        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.ADDITION,
                    quantity1DTO != null ? quantity1DTO.getValue() : 0.0,
                    quantity1DTO != null ? quantity1DTO.getUnit() : "UNKNOWN",
                    quantity1DTO != null ? quantity1DTO.getMeasurementType() : "UNKNOWN",
                    quantity2DTO != null ? quantity2DTO.getValue() : 0.0,
                    quantity2DTO != null ? quantity2DTO.getUnit() : "UNKNOWN",
                    quantity2DTO != null ? quantity2DTO.getMeasurementType() : "UNKNOWN",
                    e.getMessage());
            repository.save(errorEntity);
            return errorEntity;
        }
    }

    /**
     * Subtract one quantity from another.
     *
     * @param quantity1DTO the quantity to subtract from
     * @param quantity2DTO the quantity to subtract
     * @return a QuantityMeasurementEntity containing the difference or error
     */
    @Override
    public QuantityMeasurementEntity subtract(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO) {
        try {
            validateArithmeticOperands(quantity1DTO, quantity2DTO);

            IMeasurable unit1 = getUnitByName(quantity1DTO.getUnit(), quantity1DTO.getMeasurementType());
            IMeasurable unit2 = getUnitByName(quantity2DTO.getUnit(), quantity2DTO.getMeasurementType());

            if (!unit1.supportsArithmetic()) {
                throw new QuantityMeasurementException(
                        "Arithmetic operations are not supported for " + quantity1DTO.getMeasurementType());
            }

            double baseValue1 = unit1.convertToBaseUnit(quantity1DTO.getValue());
            double baseValue2 = unit2.convertToBaseUnit(quantity2DTO.getValue());
            double resultBase = baseValue1 - baseValue2;

            double resultValue = unit1.convertFromBaseUnit(resultBase);
            resultValue = roundToTwoDecimals(resultValue);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.SUBTRACTION,
                    quantity1DTO.getValue(),
                    quantity1DTO.getUnit(),
                    quantity1DTO.getMeasurementType(),
                    quantity2DTO.getValue(),
                    quantity2DTO.getUnit(),
                    quantity2DTO.getMeasurementType(),
                    resultValue,
                    quantity1DTO.getUnit(),
                    quantity1DTO.getMeasurementType());
            repository.save(entity);
            return entity;

        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.SUBTRACTION,
                    quantity1DTO != null ? quantity1DTO.getValue() : 0.0,
                    quantity1DTO != null ? quantity1DTO.getUnit() : "UNKNOWN",
                    quantity1DTO != null ? quantity1DTO.getMeasurementType() : "UNKNOWN",
                    quantity2DTO != null ? quantity2DTO.getValue() : 0.0,
                    quantity2DTO != null ? quantity2DTO.getUnit() : "UNKNOWN",
                    quantity2DTO != null ? quantity2DTO.getMeasurementType() : "UNKNOWN",
                    e.getMessage());
            repository.save(errorEntity);
            return errorEntity;
        }
    }

    /**
     * Divide one quantity by another.
     *
     * @param quantity1DTO the dividend quantity
     * @param quantity2DTO the divisor quantity
     * @return a QuantityMeasurementEntity containing the division result or error
     */
    @Override
    public QuantityMeasurementEntity divide(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO) {
        try {
            validateArithmeticOperands(quantity1DTO, quantity2DTO);

            IMeasurable unit1 = getUnitByName(quantity1DTO.getUnit(), quantity1DTO.getMeasurementType());
            IMeasurable unit2 = getUnitByName(quantity2DTO.getUnit(), quantity2DTO.getMeasurementType());

            double baseValue1 = unit1.convertToBaseUnit(quantity1DTO.getValue());
            double baseValue2 = unit2.convertToBaseUnit(quantity2DTO.getValue());

            if (baseValue2 == 0.0) {
                throw new ArithmeticException("Division by zero is not allowed.");
            }

            double resultValue = baseValue1 / baseValue2;
            resultValue = roundToTwoDecimals(resultValue);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.DIVISION,
                    quantity1DTO.getValue(),
                    quantity1DTO.getUnit(),
                    quantity1DTO.getMeasurementType(),
                    quantity2DTO.getValue(),
                    quantity2DTO.getUnit(),
                    quantity2DTO.getMeasurementType(),
                    resultValue,
                    "dimensionless",
                    "SCALAR");
            repository.save(entity);
            return entity;

        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    QuantityMeasurementEntity.OperationType.DIVISION,
                    quantity1DTO != null ? quantity1DTO.getValue() : 0.0,
                    quantity1DTO != null ? quantity1DTO.getUnit() : "UNKNOWN",
                    quantity1DTO != null ? quantity1DTO.getMeasurementType() : "UNKNOWN",
                    quantity2DTO != null ? quantity2DTO.getValue() : 0.0,
                    quantity2DTO != null ? quantity2DTO.getUnit() : "UNKNOWN",
                    quantity2DTO != null ? quantity2DTO.getMeasurementType() : "UNKNOWN",
                    e.getMessage());
            repository.save(errorEntity);
            return errorEntity;
        }
    }

    /**
     * Validate that both operands are suitable for arithmetic operations.
     */
    private void validateArithmeticOperands(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO) {
        if (quantity1DTO == null || quantity2DTO == null) {
            throw new IllegalArgumentException("Both quantities must not be null.");
        }
        if (!quantity1DTO.getMeasurementType().equals(quantity2DTO.getMeasurementType())) {
            throw new IllegalArgumentException(
                    "Cannot perform arithmetic between different measurement types");
        }
        if (!Double.isFinite(quantity1DTO.getValue())) {
            throw new IllegalArgumentException(
                    "First quantity's value is not finite: " + quantity1DTO.getValue());
        }
        if (!Double.isFinite(quantity2DTO.getValue())) {
            throw new IllegalArgumentException(
                    "Second quantity's value is not finite: " + quantity2DTO.getValue());
        }
    }

    /**
     * Get a unit instance by its name for a specific measurement type.
     */
    private IMeasurable getUnitByName(String unitName, String measurementType) {
        try {
            if ("LENGTH".equals(measurementType)) {
                return LengthUnit.valueOf(unitName);
            } else if ("WEIGHT".equals(measurementType)) {
                return WeightUnit.valueOf(unitName);
            } else if ("VOLUME".equals(measurementType)) {
                return VolumeUnit.valueOf(unitName);
            } else if ("TEMPERATURE".equals(measurementType)) {
                return TemperatureUnit.valueOf(unitName);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return null;
    }

    /**
     * Round a double value to two decimal places.
     */
    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
