package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class QuantityMeasurementControllerTest {

    private IQuantityMeasurementService service;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(IQuantityMeasurementService.class);
        controller = new QuantityMeasurementController(service);
    }

    @Test
    void performComparison_ShouldDelegateToService() {
        QuantityDTO first = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO second = new QuantityDTO(1000.0, "GRAM", "WEIGHT");
        QuantityMeasurementEntity expected = new QuantityMeasurementEntity("COMPARE", "true", "WEIGHT");

        when(service.compare(first, second)).thenReturn(expected);

        QuantityMeasurementEntity actual = controller.performComparison(first, second);

        assertEquals("true", actual.getResult());
        verify(service).compare(first, second);
    }

    @Test
    void performConversion_ShouldDelegateToService() {
        QuantityDTO quantity = new QuantityDTO(1.0, "LITRE", "VOLUME");
        QuantityMeasurementEntity expected = new QuantityMeasurementEntity("CONVERT", "1000.00 MILLILITRE", "VOLUME");

        when(service.convert(quantity, "MILLILITRE")).thenReturn(expected);

        QuantityMeasurementEntity actual = controller.performConversion(quantity, "MILLILITRE");

        assertEquals("1000.00 MILLILITRE", actual.getResult());
        verify(service).convert(quantity, "MILLILITRE");
    }
}
