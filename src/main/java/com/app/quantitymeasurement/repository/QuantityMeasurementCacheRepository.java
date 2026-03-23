package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementCacheRepository.class);
    private static final QuantityMeasurementCacheRepository INSTANCE = new QuantityMeasurementCacheRepository();

    private final List<QuantityMeasurementEntity> cache = Collections.synchronizedList(new ArrayList<>());

    private QuantityMeasurementCacheRepository() {
        LOGGER.info("QuantityMeasurementCacheRepository initialized");
    }

    public static QuantityMeasurementCacheRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        cache.add(entity);
        LOGGER.info("Measurement cached: {}", entity);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        synchronized (cache) {
            return List.copyOf(cache);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        synchronized (cache) {
            return cache.stream()
                    .filter(entity -> entity.getOperation().equalsIgnoreCase(operation))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        synchronized (cache) {
            return cache.stream()
                    .filter(entity -> measurementType.equalsIgnoreCase(entity.getMeasurementType()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public int getTotalCount() {
        return cache.size();
    }

    @Override
    public void deleteAll() {
        cache.clear();
        LOGGER.info("Cache repository cleared");
    }

    @Override
    public String getPoolStatistics() {
        return "Cache repository entries: " + cache.size();
    }
}
