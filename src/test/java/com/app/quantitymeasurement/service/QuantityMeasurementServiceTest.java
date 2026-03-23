package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementServiceTest {

    private IQuantityMeasurementRepository repository;
    private QuantityMeasurementServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = QuantityMeasurementCacheRepository.getInstance();
        repository.deleteAll();
        service = new QuantityMeasurementServiceImpl(repository);
    }

    @Test
    void compare_ShouldPersistSuccessfulComparison() {
        QuantityMeasurementEntity result = service.compare(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH")
        );

        assertFalse(result.hasError());
        assertEquals("true", result.getResult());
        assertEquals(1, repository.getTotalCount());
    }

    @Test
    void convert_ShouldConvertFeetToInch() {
        QuantityMeasurementEntity result = service.convert(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                "INCH"
        );

        assertFalse(result.hasError());
        assertEquals("12.00 INCH", result.getResult());
    }

    @Test
    void add_ShouldReturnTwoFeetForOneFootPlusTwelveInches() {
        QuantityMeasurementEntity result = service.add(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH")
        );

        assertFalse(result.hasError());
        assertEquals("2.00 FEET", result.getResult());
    }

    @Test
    void compare_ShouldReturnErrorForCrossCategoryComparison() {
        QuantityMeasurementEntity result = service.compare(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(1.0, "GRAM", "WEIGHT")
        );

        assertTrue(result.hasError());
        assertEquals(0, repository.getTotalCount());
    }

    @Test
    void divide_ShouldSupportSameCategoryDivision() {
        QuantityMeasurementEntity result = service.divide(
                new QuantityDTO(10.0, "FEET", "LENGTH"),
                new QuantityDTO(2.0, "FEET", "LENGTH")
        );

        assertFalse(result.hasError());
        assertEquals("5.0", result.getResult());
    }
}
