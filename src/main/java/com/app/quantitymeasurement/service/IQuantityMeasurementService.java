package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

public interface IQuantityMeasurementService {

    QuantityMeasurementEntity add(QuantityDTO quantityOne, QuantityDTO quantityTwo);

    QuantityMeasurementEntity subtract(QuantityDTO quantityOne, QuantityDTO quantityTwo);

    QuantityMeasurementEntity divide(QuantityDTO quantityOne, QuantityDTO quantityTwo);

    QuantityMeasurementEntity convert(QuantityDTO quantity, String targetUnit);

    QuantityMeasurementEntity compare(QuantityDTO quantityOne, QuantityDTO quantityTwo);
}
