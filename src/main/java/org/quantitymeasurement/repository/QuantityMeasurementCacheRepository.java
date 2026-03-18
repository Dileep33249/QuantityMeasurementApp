package org.quantitymeasurement.repository;

import org.quantitymeasurement.model.QuantityMeasurementEntity;
import org.quantitymeasurement.exception.QuantityMeasurementException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * QuantityMeasurementCacheRepository is a Singleton class that implements the
 * IQuantityMeasurementRepository interface. It provides an in-memory cache for
 * storing QuantityMeasurementEntity objects with persistence to disk.
 *
 * The repository uses ArrayList for quick access and includes methods for
 * saving entities to disk and loading them back into memory.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final String DATA_FILE_NAME = "quantity_measurement_data.dat";
    private static final String DATA_DIRECTORY = "data";

    private static QuantityMeasurementCacheRepository instance;
    private List<QuantityMeasurementEntity> cache;
    private Path dataFilePath;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private QuantityMeasurementCacheRepository() {
        this.cache = new ArrayList<>();
        initializeDataDirectory();
        this.dataFilePath = Paths.get(DATA_DIRECTORY, DATA_FILE_NAME);
        loadFromDisk();
    }

    /**
     * Get the singleton instance of QuantityMeasurementCacheRepository.
     *
     * @return the singleton instance
     */
    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    /**
     * Initialize the data directory if it does not exist.
     */
    private void initializeDataDirectory() {
        try {
            Path dirPath = Paths.get(DATA_DIRECTORY);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize data directory: " + e.getMessage());
        }
    }

    /**
     * Save a QuantityMeasurementEntity to both cache and persistent storage.
     *
     * @param entity the entity to save
     */
    @Override
    public void save(QuantityMeasurementEntity entity) {
        if (entity == null) {
            throw new QuantityMeasurementException("Cannot save a null entity.");
        }
        synchronized (cache) {
            cache.add(entity);
            saveToDisk(entity);
        }
    }

    /**
     * Save a single entity to the data file using AppendableObjectOutputStream.
     *
     * @param entity the entity to save
     */
    private void saveToDisk(QuantityMeasurementEntity entity) {
        try {
            boolean fileExists = Files.exists(dataFilePath) && Files.size(dataFilePath) > 0;
            FileOutputStream fos = new FileOutputStream(dataFilePath.toFile(), true);
            AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos, fileExists);
            oos.writeObject(entity);
            oos.close();
            fos.close();
        } catch (IOException e) {
            throw new QuantityMeasurementException("Failed to save entity to disk: " + e.getMessage(), e);
        }
    }

    /**
     * Load all entities from the data file into the in-memory cache.
     */
    private void loadFromDisk() {
        if (!Files.exists(dataFilePath)) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataFilePath.toFile()))) {
            Object obj;
            while (true) {
                try {
                    obj = ois.readObject();
                    if (obj instanceof QuantityMeasurementEntity) {
                        QuantityMeasurementEntity entity = (QuantityMeasurementEntity) obj;
                        cache.add(entity);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load entities from disk: " + e.getMessage());
        }
    }

    /**
     * Find an entity by its index/ID.
     *
     * @param id the ID of the entity
     * @return the entity, or null if not found
     */
    @Override
    public QuantityMeasurementEntity findById(int id) {
        synchronized (cache) {
            if (id >= 0 && id < cache.size()) {
                return cache.get(id);
            }
        }
        return null;
    }

    /**
     * Retrieve all stored entities.
     *
     * @return an unmodifiable list of all entities
     */
    @Override
    public List<QuantityMeasurementEntity> findAll() {
        synchronized (cache) {
            return Collections.unmodifiableList(new ArrayList<>(cache));
        }
    }

    /**
     * Get the count of stored entities.
     *
     * @return the number of entities
     */
    @Override
    public int count() {
        synchronized (cache) {
            return cache.size();
        }
    }

    /**
     * Clear all entities from the cache and disk.
     */
    @Override
    public void deleteAll() {
        synchronized (cache) {
            cache.clear();
            try {
                if (Files.exists(dataFilePath)) {
                    Files.delete(dataFilePath);
                }
            } catch (IOException e) {
                System.err.println("Failed to delete data file: " + e.getMessage());
            }
        }
    }

    /**
     * Get the data file path (for testing purposes).
     *
     * @return the Path to the data file
     */
    public Path getDataFilePath() {
        return dataFilePath;
    }
}
