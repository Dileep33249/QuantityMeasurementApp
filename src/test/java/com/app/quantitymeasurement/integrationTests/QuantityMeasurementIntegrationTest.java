package com.app.quantitymeasurement.integrationTests;

import com.app.quantitymeasurement.QuantityMeasurementApp;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class QuantityMeasurementIntegrationTest {

    private QuantityMeasurementApp app;
    private IQuantityMeasurementRepository repository;

    @BeforeEach
    void setUp() {
        System.setProperty("repository.type", "database");
        System.setProperty("app.environment", "test");
        System.setProperty("db.url", "jdbc:h2:mem:integration_test;DB_CLOSE_DELAY=-1");
        ApplicationConfig.reset();
        ConnectionPool.resetInstance();
        repository = new QuantityMeasurementDatabaseRepository();
        repository.deleteAll();
        app = new QuantityMeasurementApp(repository);
    }

    @AfterEach
    void tearDown() {
        app.closeResources();
        ConnectionPool.resetInstance();
        ApplicationConfig.reset();
        System.clearProperty("repository.type");
        System.clearProperty("app.environment");
        System.clearProperty("db.url");
    }

    @Test
    void endToEndDatabaseFlow_ShouldPersistHistory() {
        QuantityMeasurementEntity comparison = app.getController().performComparison(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH")
        );
        QuantityMeasurementEntity conversion = app.getController().performConversion(
                new QuantityDTO(1.0, "GALLON", "VOLUME"),
                "LITRE"
        );

        List<QuantityMeasurementEntity> measurements = app.reportAllMeasurements();

        assertFalse(comparison.hasError());
        assertFalse(conversion.hasError());
        assertEquals("3.79 LITRE", conversion.getResult());
        assertEquals(2, measurements.size());
        assertEquals(2, repository.getTotalCount());
    }
}
