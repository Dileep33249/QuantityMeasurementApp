package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionPool.class);
    private static volatile ConnectionPool instance;

    private final BlockingQueue<Connection> availableConnections;
    private final List<Connection> allConnections = new ArrayList<>();
    private final ApplicationConfig config;
    private final int poolSize;

    private ConnectionPool(ApplicationConfig config) {
        this.config = config;
        this.poolSize = config.getPoolSize();
        this.availableConnections = new ArrayBlockingQueue<>(poolSize);
        initializePool();
        initializeSchema();
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool(ApplicationConfig.getInstance());
                }
            }
        }
        return instance;
    }

    public static void resetInstance() {
        synchronized (ConnectionPool.class) {
            if (instance != null) {
                instance.closeAllConnections();
                instance = null;
            }
        }
    }

    public Connection acquireConnection() {
        try {
            Connection connection = availableConnections.poll(5, TimeUnit.SECONDS);
            if (connection == null) {
                throw new DatabaseException("Connection pool exhausted.");
            }
            if (connection.isClosed() || !connection.isValid(2)) {
                allConnections.remove(connection);
                connection = createConnection();
                allConnections.add(connection);
            }
            LOGGER.debug("Connection acquired. Available connections: {}", availableConnections.size());
            return connection;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new DatabaseException("Interrupted while acquiring database connection", exception);
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to validate database connection", exception);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            if (connection.isClosed()) {
                return;
            }
            if (!availableConnections.offer(connection)) {
                connection.close();
            }
            LOGGER.debug("Connection released. Available connections: {}", availableConnections.size());
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to release database connection", exception);
        }
    }

    public void closeAllConnections() {
        for (Connection connection : new ArrayList<>(allConnections)) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException exception) {
                LOGGER.warn("Error while closing connection: {}", exception.getMessage());
            }
        }
        allConnections.clear();
        availableConnections.clear();
        LOGGER.info("All pooled connections closed");
    }

    public String getStatistics() {
        return String.format(
                "Pool size: %d | Available: %d | In use: %d",
                poolSize,
                availableConnections.size(),
                poolSize - availableConnections.size()
        );
    }

    private void initializePool() {
        try {
            Class.forName(config.getDbDriver());
            for (int index = 0; index < poolSize; index++) {
                Connection connection = createConnection();
                allConnections.add(connection);
                availableConnections.offer(connection);
            }
            LOGGER.info("Connection pool initialized with {} connections", poolSize);
        } catch (ClassNotFoundException | SQLException exception) {
            throw new DatabaseException("Failed to initialize connection pool", exception);
        }
    }

    private Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                config.getDbUrl(),
                config.getDbUsername(),
                config.getDbPassword()
        );
        connection.setAutoCommit(true);
        return connection;
    }

    private void initializeSchema() {
        String resource = config.getSchemaResource();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new DatabaseException("Schema resource not found: " + resource);
            }
            String script = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            try (Connection connection = createConnection(); Statement statement = connection.createStatement()) {
                for (String sql : script.split(";")) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty()) {
                        statement.execute(trimmed);
                    }
                }
            }
            LOGGER.info("Database schema initialized from {}", resource);
        } catch (IOException | SQLException exception) {
            throw new DatabaseException("Failed to initialize database schema", exception);
        }
    }
}
