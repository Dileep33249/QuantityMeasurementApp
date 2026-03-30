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
        repository.save(entity("alice", "compare", "LengthUnit", false, null));
        repository.save(entity("alice", "compare", "LengthUnit", true, "boom"));
        repository.save(entity("bob", "compare", "LengthUnit", false, null));
        repository.save(entity("alice", "add", "LengthUnit", false, null));

        List<QuantityMeasurementEntity> compareHistory =
                repository.findByCreatedByUsernameIgnoreCaseAndOperationIgnoreCase("ALICE", "COMPARE");
        long compareCount =
                repository.countByCreatedByUsernameIgnoreCaseAndOperationIgnoreCaseAndErrorFalse("ALICE", "COMPARE");

        assertThat(compareHistory).hasSize(2);
        assertThat(compareCount).isEqualTo(1);
    }

    @Test
    void shouldFindByMeasurementTypeCreatedAtAndError() {
        QuantityMeasurementEntity length = repository.save(entity("alice", "add", "LengthUnit", false, null));
        QuantityMeasurementEntity weightError = repository.save(entity("alice", "subtract", "WeightUnit", true, "bad data"));
        repository.save(entity("bob", "add", "LengthUnit", false, null));

        List<QuantityMeasurementEntity> byType =
                repository.findByCreatedByUsernameIgnoreCaseAndThisMeasurementTypeIgnoreCase("alice", "lengthunit");
        List<QuantityMeasurementEntity> recent = repository.findByCreatedAtAfter(LocalDateTime.now().minusMinutes(1));
        List<QuantityMeasurementEntity> errored = repository.findErroredByCreatedByUsername("alice");

        assertThat(byType).extracting(QuantityMeasurementEntity::getId).contains(length.getId());
        assertThat(recent).extracting(QuantityMeasurementEntity::getId).contains(length.getId(), weightError.getId());
        assertThat(errored).extracting(QuantityMeasurementEntity::getId).contains(weightError.getId());
    }

    private QuantityMeasurementEntity entity(String username, String operation, String measurementType, boolean error, String errorMessage) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(1.0);
        entity.setThisUnit("FEET");
        entity.setThisMeasurementType(measurementType);
        entity.setThatValue(12.0);
        entity.setThatUnit("INCHES");
        entity.setThatMeasurementType(measurementType);
        entity.setCreatedByUsername(username);
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
