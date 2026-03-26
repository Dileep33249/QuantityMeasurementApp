package com.app.quantitymeasurement;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuantityMeasurementRepositoryTest {

    @Autowired
    private QuantityMeasurementRepository repository;

    @Test
    void shouldFindByOperationAndCountSuccessfulOperations() {
        repository.save(entity("compare", "LengthUnit", false, null));
        repository.save(entity("compare", "LengthUnit", true, "boom"));
        repository.save(entity("add", "LengthUnit", false, null));

        List<QuantityMeasurementEntity> compareHistory = repository.findByOperationIgnoreCase("COMPARE");
        List<QuantityMeasurementEntity> successfulCompareHistory = repository.findSuccessfulByOperation("COMPARE");
        long compareCount = repository.countByOperationIgnoreCaseAndErrorFalse("COMPARE");

        assertThat(compareHistory).hasSize(2);
        assertThat(successfulCompareHistory).hasSize(1);
        assertThat(compareCount).isEqualTo(1);
    }

    @Test
    void shouldFindByMeasurementTypeCreatedAtAndError() {
        QuantityMeasurementEntity length = repository.save(entity("add", "LengthUnit", false, null));
        QuantityMeasurementEntity weightError = repository.save(entity("subtract", "WeightUnit", true, "bad data"));

        List<QuantityMeasurementEntity> byType = repository.findByThisMeasurementTypeIgnoreCase("lengthunit");
        List<QuantityMeasurementEntity> recent = repository.findByCreatedAtAfter(LocalDateTime.now().minusMinutes(1));
        List<QuantityMeasurementEntity> errored = repository.findByErrorTrue();

        assertThat(byType).extracting(QuantityMeasurementEntity::getId).contains(length.getId());
        assertThat(recent).extracting(QuantityMeasurementEntity::getId).contains(length.getId(), weightError.getId());
        assertThat(errored).extracting(QuantityMeasurementEntity::getId).contains(weightError.getId());
    }

    private QuantityMeasurementEntity entity(String operation, String measurementType, boolean error, String errorMessage) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(1.0);
        entity.setThisUnit("FEET");
        entity.setThisMeasurementType(measurementType);
        entity.setThatValue(12.0);
        entity.setThatUnit("INCHES");
        entity.setThatMeasurementType(measurementType);
        entity.setOperation(operation);
        entity.setResultString("true");
        entity.setResultValue(1.0);
        entity.setResultUnit("FEET");
        entity.setResultMeasurementType(measurementType);
        entity.setError(error);
        entity.setErrorMessage(errorMessage);
        return entity;
    }
}
