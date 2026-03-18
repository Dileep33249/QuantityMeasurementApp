package org.quantitymeasurement.unit;
import org.quantitymeasurement.controller.QuantityMeasurementController;
import org.quantitymeasurement.service.IQuantityMeasurementService;
import org.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import org.quantitymeasurement.repository.IQuantityMeasurementRepository;
import org.quantitymeasurement.repository.QuantityMeasurementCacheRepository;

/**
 * QuantityMeasurementApp (UC15) is the main application class and entry point for the
 * Quantity Measurement system. This class implements the N-Tier architecture by:
 *
 * 1. Initializing the repository layer (QuantityMeasurementCacheRepository).
 * 2. Creating the service layer (QuantityMeasurementServiceImpl).
 * 3. Instantiating the controller layer (QuantityMeasurementController).
 * 4. Delegating all business logic to the controller.
 *
 * This class is a singleton and serves as a Factory for creating layer instances
 * and a Facade for presenting the simplified application interface.
 *
 * Design Patterns Used:
 * - Factory Design Pattern: Creates instances of controllers and services
 * - Facade Design Pattern: Provides a simplified interface for operations
 * - Singleton Design Pattern: Ensures single application instance
 * - Dependency Injection: Promotes loose coupling between layers
 */
public class QuantityMeasurementApp {

    private static QuantityMeasurementApp instance;
    private final IQuantityMeasurementRepository repository;
    private final IQuantityMeasurementService service;
    private final QuantityMeasurementController controller;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes all layers through dependency injection.
     */
    private QuantityMeasurementApp() {
        // Initialize Repository Layer (Singleton)
        this.repository = QuantityMeasurementCacheRepository.getInstance();

        // Initialize Service Layer with repository
        this.service = new QuantityMeasurementServiceImpl(repository);

        // Initialize Controller Layer with service
        this.controller = new QuantityMeasurementController(service);
    }

    /**
     * Get the singleton instance of QuantityMeasurementApp.
     *
     * @return the singleton instance
     */
    public static synchronized QuantityMeasurementApp getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementApp();
        }
        return instance;
    }

    /**
     * Get the controller instance.
     *
     * @return the QuantityMeasurementController
     */
    public QuantityMeasurementController getController() {
        return controller;
    }

    /**
     * Get the service instance.
     *
     * @return the IQuantityMeasurementService
     */
    public IQuantityMeasurementService getService() {
        return service;
    }

    /**
     * Get the repository instance.
     *
     * @return the IQuantityMeasurementRepository
     */
    public IQuantityMeasurementRepository getRepository() {
        return repository;
    }

    /**
     * Run demonstrations for all quantity measurement operations.
     */
    public void runDemonstrations() {
        controller.demonstrateEquality();
        controller.demonstrateConversion();
        controller.demonstrateAddition();
        controller.demonstrateSubtraction();
        controller.demonstrateDivision();
        controller.demonstrateErrors();
    }

    /**
     * Main entry point for the application.
     * Initializes the application and runs all demonstrations.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Get the singleton instance
        QuantityMeasurementApp app = QuantityMeasurementApp.getInstance();

        // Run all demonstrations
        app.runDemonstrations();
    }
}

