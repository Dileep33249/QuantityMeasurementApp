package org.quantitymeasurement.repository;

import org.quantitymeasurement.model.QuantityMeasurementEntity;
import java.util.List;

/**
 * IQuantityMeasurementRepository interface serves as the data access layer.
 * This interface defines the contract for data access operations related to
 * QuantityMeasurement entities, following the Interface Segregation Principle.
 */
public interface IQuantityMeasurementRepository {

    /**
     * Save a QuantityMeasurementEntity to the repository.
     *
     * @param entity the entity to save
     */
    void save(QuantityMeasurementEntity entity);

    /**
     * Find a QuantityMeasurementEntity by its ID/index.
     *
     * @param id the ID of the entity
     * @return the entity, or null if not found
     */
    QuantityMeasurementEntity findById(int id);

    /**
     * Retrieve all stored QuantityMeasurementEntity objects.
     *
     * @return a list of all stored entities
     */
    List<QuantityMeasurementEntity> findAll();

    /**
     * Get the count of stored entities.
     *
     * @return the number of entities in the repository
     */
    int count();

    /**
     * Clear all stored entities from the repository.
     */
    void deleteAll();
}
