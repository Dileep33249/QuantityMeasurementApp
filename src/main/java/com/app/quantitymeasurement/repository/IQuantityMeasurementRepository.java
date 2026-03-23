package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();

    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation);

    List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType);

    int getTotalCount();

    void deleteAll();

    default List<QuantityMeasurementEntity> findAll() {
        return getAllMeasurements();
    }

    default List<QuantityMeasurementEntity> findByOperation(String operation) {
        return getMeasurementsByOperation(operation);
    }

    default List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
        return getMeasurementsByType(measurementType);
    }

    default String getPoolStatistics() {
        return "Pool statistics not available for this repository.";
    }

    default void releaseResources() {
    }
}
