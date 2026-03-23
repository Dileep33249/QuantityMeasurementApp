package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository() {
        this(ConnectionPool.getInstance());
    }

    public QuantityMeasurementDatabaseRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        LOGGER.info("QuantityMeasurementDatabaseRepository initialized");
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        String insertEntitySql = "INSERT INTO quantity_measurement_entity " +
                "(operation, result, measurement_type, is_error, created_at) VALUES (?, ?, ?, ?, ?)";
        String insertHistorySql = "INSERT INTO quantity_measurement_history " +
                "(operation, result, measurement_type, is_error, created_at) VALUES (?, ?, ?, ?, ?)";

        Connection connection = connectionPool.acquireConnection();
        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            saveMeasurement(connection, insertEntitySql, entity, true);
            saveMeasurement(connection, insertHistorySql, entity, false);

            connection.commit();
            LOGGER.info("Measurement persisted to database: {}", entity);
        } catch (SQLException exception) {
            rollbackQuietly(connection);
            throw new DatabaseException("Failed to save measurement", exception);
        } finally {
            resetAutoCommit(connection, originalAutoCommit);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        String sql = "SELECT id, operation, result, measurement_type, is_error, created_at " +
                "FROM quantity_measurement_entity ORDER BY created_at DESC, id DESC";
        return fetchMeasurements(sql, null);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        String sql = "SELECT id, operation, result, measurement_type, is_error, created_at " +
                "FROM quantity_measurement_entity WHERE operation = ? ORDER BY created_at DESC, id DESC";
        return fetchMeasurements(sql, statement -> statement.setString(1, operation));
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        String sql = "SELECT id, operation, result, measurement_type, is_error, created_at " +
                "FROM quantity_measurement_entity WHERE measurement_type = ? ORDER BY created_at DESC, id DESC";
        return fetchMeasurements(sql, statement -> statement.setString(1, measurementType));
    }

    @Override
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM quantity_measurement_entity";
        Connection connection = connectionPool.acquireConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to fetch total count", exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void deleteAll() {
        Connection connection = connectionPool.acquireConnection();
        boolean originalAutoCommit = true;
        try (PreparedStatement deleteEntity = connection.prepareStatement("DELETE FROM quantity_measurement_entity");
             PreparedStatement deleteHistory = connection.prepareStatement("DELETE FROM quantity_measurement_history")) {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            deleteEntity.executeUpdate();
            deleteHistory.executeUpdate();
            connection.commit();
            LOGGER.info("All measurements removed from database");
        } catch (SQLException exception) {
            rollbackQuietly(connection);
            throw new DatabaseException("Failed to delete measurements", exception);
        } finally {
            resetAutoCommit(connection, originalAutoCommit);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public String getPoolStatistics() {
        return connectionPool.getStatistics();
    }

    @Override
    public void releaseResources() {
        connectionPool.closeAllConnections();
    }

    private void saveMeasurement(Connection connection, String sql, QuantityMeasurementEntity entity, boolean populateId)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getOperation());
            statement.setString(2, entity.getResult());
            statement.setString(3, entity.getMeasurementType());
            statement.setBoolean(4, entity.hasError());
            statement.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));
            statement.executeUpdate();

            if (populateId) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }

    private List<QuantityMeasurementEntity> fetchMeasurements(String sql, StatementConfigurer configurer) {
        Connection connection = connectionPool.acquireConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (configurer != null) {
                configurer.configure(statement);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to fetch measurements", exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private List<QuantityMeasurementEntity> mapResultSet(ResultSet resultSet) throws SQLException {
        List<QuantityMeasurementEntity> measurements = new ArrayList<>();
        while (resultSet.next()) {
            boolean error = resultSet.getBoolean("is_error");
            String operation = resultSet.getString("operation");
            String result = resultSet.getString("result");
            String measurementType = resultSet.getString("measurement_type");

            QuantityMeasurementEntity entity = error
                    ? new QuantityMeasurementEntity(result)
                    : new QuantityMeasurementEntity(operation, result, measurementType);
            entity.setId(resultSet.getLong("id"));
            Timestamp createdAt = resultSet.getTimestamp("created_at");
            entity.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : LocalDateTime.now());
            if (error) {
                entity.setMeasurementType(measurementType);
            }
            measurements.add(entity);
        }
        return measurements;
    }

    private void rollbackQuietly(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException exception) {
            LOGGER.warn("Rollback failed: {}", exception.getMessage());
        }
    }

    private void resetAutoCommit(Connection connection, boolean originalAutoCommit) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException exception) {
            LOGGER.warn("Failed to reset auto-commit: {}", exception.getMessage());
        }
    }

    @FunctionalInterface
    private interface StatementConfigurer {
        void configure(PreparedStatement statement) throws SQLException;
    }
}
