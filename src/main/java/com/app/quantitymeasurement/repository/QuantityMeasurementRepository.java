package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperationIgnoreCase(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementTypeIgnoreCase(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime createdAt);

    @Query("select q from QuantityMeasurementEntity q where upper(q.operation) = upper(?1) and q.error = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(String operation);

    long countByOperationIgnoreCaseAndErrorFalse(String operation);

    List<QuantityMeasurementEntity> findByErrorTrue();
}
