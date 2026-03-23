package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementDatabaseRepositoryTest {

    private QuantityMeasurementDatabaseRepository repository;

    @BeforeEach
    void setUp() {
        System.setProperty("repository.type", "database");
        System.setProperty("app.environment", "test");
        System.setProperty("db.url", "jdbc:h2:mem:repo_test;DB_CLOSE_DELAY=-1");
        ApplicationConfig.reset();
        ConnectionPool.resetInstance();
        repository = new QuantityMeasurementDatabaseRepository();
        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        repository.releaseResources();
        ConnectionPool.resetInstance();
        ApplicationConfig.reset();
        System.clearProperty("repository.type");
        System.clearProperty("app.environment");
        System.clearProperty("db.url");
    }

    @Test
    void save_ShouldPersistMeasurementAndHistory() throws Exception {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity("CONVERT", "12.00 INCH", "LENGTH");

        repository.save(entity);

        assertNotNull(entity.getId());
        assertEquals(1, repository.getTotalCount());
        assertEquals(1, repository.getMeasurementsByOperation("CONVERT").size());

        try (Connection connection = ConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM quantity_measurement_history");
             ResultSet resultSet = statement.executeQuery()) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
        }
    }

    @Test
    void getMeasurementsByType_ShouldFilterResults() {
        repository.save(new QuantityMeasurementEntity("COMPARE", "true", "LENGTH"));
        repository.save(new QuantityMeasurementEntity("CONVERT", "1000.00 MILLILITRE", "VOLUME"));

        assertEquals(1, repository.getMeasurementsByType("LENGTH").size());
        assertEquals(1, repository.getMeasurementsByType("VOLUME").size());
    }

    @Test
    void deleteAll_ShouldRemoveEntities() {
        repository.save(new QuantityMeasurementEntity("ADD", "2.00 FEET", "LENGTH"));
        repository.save(new QuantityMeasurementEntity("SUBTRACT", "9.50 FEET", "LENGTH"));

        repository.deleteAll();

        assertEquals(0, repository.getTotalCount());
    }

    @Test
    void getPoolStatistics_ShouldExposeConnectionPoolInfo() {
        String statistics = repository.getPoolStatistics();

        assertTrue(statistics.contains("Pool size:"));
        assertTrue(statistics.contains("Available:"));
    }
}
