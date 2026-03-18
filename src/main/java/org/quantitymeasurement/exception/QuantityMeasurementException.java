package org.quantitymeasurement.exception;

/**
 * QuantityMeasurementException is a custom exception class used to handle
 * errors
 * and exceptional conditions related to quantity measurement operations.
 *
 * This exception can be thrown when invalid measurements, unit conversions,
 * or other quantity-related operations fail during execution.
 *
 * This class extends RuntimeException, making it an unchecked exception.
 * This design allows developers to throw this exception without requiring
 * explicit try-catch blocks, while still providing the option to catch it if
 * desired.
 */
public class QuantityMeasurementException extends RuntimeException {

    /**
     * Constructor with a message.
     *
     * @param message the detail message
     */
    public QuantityMeasurementException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     *
     * @param cause the cause of the exception
     */
    public QuantityMeasurementException(Throwable cause) {
        super(cause);
    }
}
