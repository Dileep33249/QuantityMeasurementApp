package com.app.quantitymeasurement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final String DEFAULT_ENVIRONMENT = "dev";

    private static volatile ApplicationConfig instance;

    private final Properties properties = new Properties();

    private ApplicationConfig() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                LOGGER.info("Loaded application.properties");
            } else {
                LOGGER.warn("application.properties not found, using default settings");
            }
        } catch (IOException exception) {
            LOGGER.error("Unable to load application.properties: {}", exception.getMessage());
        }
    }

    public static ApplicationConfig getInstance() {
        if (instance == null) {
            synchronized (ApplicationConfig.class) {
                if (instance == null) {
                    instance = new ApplicationConfig();
                }
            }
        }
        return instance;
    }

    public static void reset() {
        synchronized (ApplicationConfig.class) {
            instance = null;
        }
    }

    public String get(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return properties.getProperty(key, defaultValue);
    }

    public String getEnvironment() {
        return get("app.environment", DEFAULT_ENVIRONMENT);
    }

    public String getRepositoryType() {
        return get("repository.type", "cache");
    }

    public String getDbUrl() {
        return resolveDbProperty("url", "jdbc:h2:mem:quantitydb;DB_CLOSE_DELAY=-1");
    }

    public String getDbUsername() {
        return resolveDbProperty("username", "sa");
    }

    public String getDbPassword() {
        return resolveDbProperty("password", "");
    }

    public String getDbDriver() {
        return resolveDbProperty("driver", "org.h2.Driver");
    }

    public int getPoolSize() {
        return Integer.parseInt(get("db.pool.size", "5"));
    }

    public String getSchemaResource() {
        return get("db.schema.resource", "db/schema.sql");
    }

    private String resolveDbProperty(String suffix, String defaultValue) {
        String environment = getEnvironment();
        String directValue = get("db." + suffix, null);
        if (directValue != null) {
            return directValue;
        }
        return get("db." + environment + "." + suffix, defaultValue);
    }
}
