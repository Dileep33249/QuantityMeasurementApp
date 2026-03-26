package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.dto.QuantityInputDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/quantities", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    @PostMapping(value = "/compare", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Compare two quantities")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(@Valid @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.compareQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
    }

    @PostMapping(value = "/convert", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Convert quantity to target unit")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(@Valid @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.convertQuantity(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(@Valid @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.addQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
    }

    @PostMapping(value = "/subtract", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Subtract two quantities")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(@Valid @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.subtractQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
    }

    @PostMapping(value = "/multiply", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Multiply two quantities")
    public ResponseEntity<QuantityMeasurementDTO> multiplyQuantities(@Valid @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.multiplyQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
    }

    @PostMapping(value = "/divide", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Divide two quantities")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(@Valid @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.divideQuantities(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get operation history")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(@PathVariable String operation) {
        return ResponseEntity.ok(service.getOperationHistory(operation));
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get history by measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementHistory(@PathVariable String measurementType) {
        return ResponseEntity.ok(service.getMeasurementHistory(measurementType));
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get error history")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErroredHistory() {
        return ResponseEntity.ok(service.getErroredHistory());
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get successful operation count")
    public ResponseEntity<Map<String, Object>> getOperationCount(@PathVariable String operation) {
        return ResponseEntity.ok(Map.of("operation", operation, "count", service.getOperationCount(operation)));
    }
}
