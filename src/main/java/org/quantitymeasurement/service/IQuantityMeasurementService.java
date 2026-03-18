package org.quantitymeasurement.service;

import org.quantitymeasurement.model.QuantityDTO;
import org.quantitymeasurement.model.QuantityMeasurementEntity;

/**
 * IQuantityMeasurementService interface provides contract methods for
 * performing
 * quantity measurement operations including conversion, comparison, and
 * arithmetic.
 *
 * This service interface defines operations for working with quantities and
 * their measurements.
 * It accepts input in the form of QuantityDTO objects and returns results as
 * QuantityMeasurementEntity objects,
 * providing a standardized API contract.
 *
 * Supported operations:
 * - Conversion: Convert quantities from one unit to another
 * - Comparison: Compare two quantities
 * - Addition: Add two quantities
 * - Subtraction: Subtract one quantity from another
 * - Division: Divide quantities
 */
public interface IQuantityMeasurementService {

    /**
     * Convert a quantity from one unit to another.
     *
     * @param quantityDTO the quantity to convert
     * @param targetUnit  the target unit for conversion
     * @return a QuantityMeasurementEntity containing the conversion result or error
     */
    QuantityMeasurementEntity convert(QuantityDTO quantityDTO, String targetUnit);

    /**
     * Compare two quantities for equality.
     *
     * @param quantity1DTO the first quantity
     * @param quantity2DTO the second quantity
     * @return a QuantityMeasurementEntity indicating if quantities are equal
     */
    QuantityMeasurementEntity compare(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO);

    /**
     * Add two quantities together.
     *
     * @param quantity1DTO the first quantity
     * @param quantity2DTO the second quantity
     * @return a QuantityMeasurementEntity containing the sum or error
     */
    QuantityMeasurementEntity add(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO);

    /**
     * Subtract one quantity from another.
     *
     * @param quantity1DTO the quantity to subtract from
     * @param quantity2DTO the quantity to subtract
     * @return a QuantityMeasurementEntity containing the difference or error
     */
    QuantityMeasurementEntity subtract(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO);

    /**
     * Divide one quantity by another.
     *
     * @param quantity1DTO the dividend quantity
     * @param quantity2DTO the divisor quantity
     * @return a QuantityMeasurementEntity containing the division result or error
     */
    QuantityMeasurementEntity divide(QuantityDTO quantity1DTO, QuantityDTO quantity2DTO);
}
