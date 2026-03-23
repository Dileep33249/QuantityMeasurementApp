package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuantityMeasurementApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    private final IQuantityMeasurementRepository repository;
    private final QuantityMeasurementController controller;

    public QuantityMeasurementApp() {
        this(createRepository(ApplicationConfig.getInstance()));
    }

    public QuantityMeasurementApp(IQuantityMeasurementRepository repository) {
        this.repository = repository;
        this.controller = new QuantityMeasurementController(new QuantityMeasurementServiceImpl(repository));
        LOGGER.info("QuantityMeasurementApp initialized with repository {}", repository.getClass().getSimpleName());
    }

    private static IQuantityMeasurementRepository createRepository(ApplicationConfig config) {
        if ("database".equalsIgnoreCase(config.getRepositoryType())) {
            LOGGER.info("Using JDBC database repository");
            return new QuantityMeasurementDatabaseRepository();
        }
        LOGGER.info("Using in-memory cache repository");
        return QuantityMeasurementCacheRepository.getInstance();
    }

    public QuantityMeasurementController getController() {
        return controller;
    }

    public IQuantityMeasurementRepository getRepository() {
        return repository;
    }

    public void deleteAllMeasurements() {
        repository.deleteAll();
        LOGGER.info("All measurements deleted");
    }

    public void closeResources() {
        repository.releaseResources();
        LOGGER.info("Repository resources released");
    }

    public List<QuantityMeasurementEntity> reportAllMeasurements() {
        List<QuantityMeasurementEntity> measurements = repository.getAllMeasurements();
        LOGGER.info("Total measurements stored: {}", measurements.size());
        measurements.forEach(measurement -> LOGGER.info("Stored measurement -> {}", measurement));
        LOGGER.info("Repository statistics: {}", repository.getPoolStatistics());
        return measurements;
    }

    public static void main(String[] args) {
        QuantityMeasurementApp app = new QuantityMeasurementApp();
        try {
            QuantityDTO quantityOne = new QuantityDTO(10, "FEET", "LENGTH");
            QuantityDTO quantityTwo = new QuantityDTO(12, "INCH", "LENGTH");

            app.getController().performAddition(quantityOne, quantityTwo);
            app.getController().performSubtraction(quantityOne, quantityTwo);
            app.getController().performDivision(quantityOne, quantityTwo);
            app.getController().performConversion(quantityOne, "INCH");
            app.getController().performComparison(quantityOne, quantityTwo);

            app.reportAllMeasurements();
            app.deleteAllMeasurements();
        } finally {
            app.closeResources();
        }
    }
}
