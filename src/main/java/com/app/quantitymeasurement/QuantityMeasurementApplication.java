package com.app.quantitymeasurement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Quantity Measurement API",
                version = "v1",
                description = "REST API for quantity comparisons, conversions, arithmetic, and history",
                contact = @Contact(name = "UC17")
        ),
        servers = @Server(url = "/", description = "Default server")
)
public class QuantityMeasurementApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApplication.class, args);
    }
}
