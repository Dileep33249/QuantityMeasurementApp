package org.quantitymeasurement.controller;

import org.quantitymeasurement.model.QuantityDTO;
import org.quantitymeasurement.model.QuantityMeasurementEntity;
import org.quantitymeasurement.service.IQuantityMeasurementService;

/**
 * QuantityMeasurementController serves as the controller layer for the
 * application.
 * It acts as the orchestration layer between user input and the service layer.
 *
 * This controller handles requests related to quantity measurements,
 * including comparison, conversion, and arithmetic operations.
 *
 * The controller is designed to be easily mappable to REST endpoints for future
 * HTTP-based consumption, with methods like performCompare, performConvert,
 * etc.
 *
 * Dependency injection is utilized to inject the QuantityMeasurementService
 * dependency,
 * promoting loose coupling and improving testability.
 */
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    /**
     * Constructor with dependency injection.
     *
     * @param service the service instance for business logic
     */
    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service must not be null.");
        }
        this.service = service;
    }

    /**
     * Perform a conversion operation.
     * Maps to: POST /api/quantity/convert
     */
    public QuantityMeasurementEntity performConvert(QuantityDTO quantity, String targetUnit) {
        validateInput(quantity);
        if (targetUnit == null || targetUnit.isEmpty()) {
            throw new IllegalArgumentException("Target unit must not be null or empty.");
        }
        return service.convert(quantity, targetUnit);
    }

    /**
     * Perform a comparison operation.
     * Maps to: POST /api/quantity/compare
     */
    public QuantityMeasurementEntity performCompare(QuantityDTO quantity1, QuantityDTO quantity2) {
        validateInput(quantity1);
        validateInput(quantity2);
        return service.compare(quantity1, quantity2);
    }

    /**
     * Perform an addition operation.
     * Maps to: POST /api/quantity/add
     */
    public QuantityMeasurementEntity performAdd(QuantityDTO quantity1, QuantityDTO quantity2) {
        validateInput(quantity1);
        validateInput(quantity2);
        return service.add(quantity1, quantity2);
    }

    /**
     * Perform a subtraction operation.
     * Maps to: POST /api/quantity/subtract
     */
    public QuantityMeasurementEntity performSubtract(QuantityDTO quantity1, QuantityDTO quantity2) {
        validateInput(quantity1);
        validateInput(quantity2);
        return service.subtract(quantity1, quantity2);
    }

    /**
     * Perform a division operation.
     * Maps to: POST /api/quantity/divide
     */
    public QuantityMeasurementEntity performDivide(QuantityDTO quantity1, QuantityDTO quantity2) {
        validateInput(quantity1);
        validateInput(quantity2);
        return service.divide(quantity1, quantity2);
    }

    /**
     * Demonstrate equality comparison with detailed output.
     */
    public void demonstrateEquality() {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  Equality Comparison Demonstration");
        System.out.println("═══════════════════════════════════════════════════\n");

        QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityMeasurementEntity result = performCompare(qty1, qty2);
        displayResult(result);

        qty1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        qty2 = new QuantityDTO(1000.0, "GRAM", "WEIGHT");
        result = performCompare(qty1, qty2);
        displayResult(result);
    }

    /**
     * Demonstrate conversion with detailed output.
     */
    public void demonstrateConversion() {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  Conversion Demonstration");
        System.out.println("═══════════════════════════════════════════════════\n");

        QuantityDTO qty = new QuantityDTO(5.0, "FEET", "LENGTH");
        QuantityMeasurementEntity result = performConvert(qty, "METRES");
        displayResult(result);

        qty = new QuantityDTO(1.0, "LITRE", "VOLUME");
        result = performConvert(qty, "MILLILITRE");
        displayResult(result);

        qty = new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE");
        result = performConvert(qty, "FAHRENHEIT");
        displayResult(result);
    }

    /**
     * Demonstrate addition with detailed output.
     */
    public void demonstrateAddition() {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  Addition Demonstration");
        System.out.println("═══════════════════════════════════════════════════\n");

        QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityMeasurementEntity result = performAdd(qty1, qty2);
        displayResult(result);

        qty1 = new QuantityDTO(10.0, "KILOGRAM", "WEIGHT");
        qty2 = new QuantityDTO(5000.0, "GRAM", "WEIGHT");
        result = performAdd(qty1, qty2);
        displayResult(result);

        qty1 = new QuantityDTO(1.0, "LITRE", "VOLUME");
        qty2 = new QuantityDTO(500.0, "MILLILITRE", "VOLUME");
        result = performAdd(qty1, qty2);
        displayResult(result);
    }

    /**
     * Demonstrate subtraction with detailed output.
     */
    public void demonstrateSubtraction() {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  Subtraction Demonstration");
        System.out.println("═══════════════════════════════════════════════════\n");

        QuantityDTO qty1 = new QuantityDTO(10.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(6.0, "INCHES", "LENGTH");
        QuantityMeasurementEntity result = performSubtract(qty1, qty2);
        displayResult(result);

        qty1 = new QuantityDTO(5.0, "LITRE", "VOLUME");
        qty2 = new QuantityDTO(2.0, "LITRE", "VOLUME");
        result = performSubtract(qty1, qty2);
        displayResult(result);
    }

    /**
     * Demonstrate division with detailed output.
     */
    public void demonstrateDivision() {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  Division Demonstration");
        System.out.println("═══════════════════════════════════════════════════\n");

        QuantityDTO qty1 = new QuantityDTO(10.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(2.0, "FEET", "LENGTH");
        QuantityMeasurementEntity result = performDivide(qty1, qty2);
        displayResult(result);

        qty1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        qty2 = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        result = performDivide(qty1, qty2);
        displayResult(result);
    }

    /**
     * Demonstrate error handling with detailed output.
     */
    public void demonstrateErrors() {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  Error Handling Demonstration");
        System.out.println("═══════════════════════════════════════════════════\n");

        // Cross-category error
        System.out.println("Cross-category operation error:");
        try {
            QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
            QuantityDTO qty2 = new QuantityDTO(1.0, "GRAM", "WEIGHT");
            QuantityMeasurementEntity result = performAdd(qty1, qty2);
            displayResult(result);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage() + "\n");
        }

        // Temperature arithmetic error
        System.out.println("Temperature arithmetic error:");
        try {
            QuantityDTO qty1 = new QuantityDTO(20.0, "CELSIUS", "TEMPERATURE");
            QuantityDTO qty2 = new QuantityDTO(10.0, "CELSIUS", "TEMPERATURE");
            QuantityMeasurementEntity result = performAdd(qty1, qty2);
            displayResult(result);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage() + "\n");
        }

        // Division by zero
        System.out.println("Division by zero error:");
        try {
            QuantityDTO qty1 = new QuantityDTO(10.0, "FEET", "LENGTH");
            QuantityDTO qty2 = new QuantityDTO(0.0, "FEET", "LENGTH");
            QuantityMeasurementEntity result = performDivide(qty1, qty2);
            displayResult(result);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage() + "\n");
        }
    }

    /**
     * Display operation result with formatted output.
     *
     * @param result the QuantityMeasurementEntity result
     */
    public void displayResult(QuantityMeasurementEntity result) {
        if (result == null) {
            System.out.println("[No result to display]\n");
            return;
        }

        if (result.hasError()) {
            System.out.println("[" + result.getOperationType() + "] ERROR: " + result.getErrorMessage());
        } else {
            String operand1 = result.getOperand1Value() + " " + result.getOperand1Unit();

            if (result.getOperand2Value() != null) {
                String operand2 = result.getOperand2Value() + " " + result.getOperand2Unit();
                System.out.println("[" + result.getOperationType() + "] "
                        + operand1 + " OP " + operand2
                        + " → " + result.getResultValue() + " " + result.getResultUnit());
            } else {
                System.out.println("[" + result.getOperationType() + "] "
                        + operand1 + " → " + result.getResultValue() + " " + result.getResultUnit());
            }
        }
        System.out.println();
    }

    /**
     * Validate input QuantityDTO.
     */
    private void validateInput(QuantityDTO quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity must not be null.");
        }
    }
}
