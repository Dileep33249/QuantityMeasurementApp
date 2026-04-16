package com.app.quantitymeasurement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "quantity-measurement-app");
        response.put("status", "running");
        response.put("swagger", "/swagger-ui.html");
        response.put("health", "/actuator/health");
        response.put("h2Console", "/h2-console");
        response.put("apiBase", "/api/v1/quantities");
        response.put("message", "Use POST endpoints for operations and GET endpoints for history/count.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api")
    public ResponseEntity<Map<String, Object>> apiHome() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("version", "v1");
        response.put("basePath", "/api/v1/quantities");
        response.put("operations", List.of("compare", "convert", "add", "subtract", "multiply", "divide"));
        return ResponseEntity.ok(response);
    }
}
