package com.app.quantitymeasurement;

import com.app.quantitymeasurement.dto.AuthResponse;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityInputDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.MeasurementType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuantityMeasurementApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(port).isPositive();
    }

    @Test
    void shouldRequireAuthenticationForQuantityApis() {
        QuantityInputDTO input = input(1.0, "FEET", MeasurementType.LengthUnit, 12.0, "INCHES", MeasurementType.LengthUnit);

        ResponseEntity<Map> response = restTemplate.postForEntity(url("/api/v1/quantities/compare"), input, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldCompareAndPersistHistory() {
        HttpHeaders authHeaders = authHeaders();
        QuantityInputDTO input = input(1.0, "FEET", MeasurementType.LengthUnit, 12.0, "INCHES", MeasurementType.LengthUnit);

        ResponseEntity<QuantityMeasurementDTO> compareResponse = restTemplate.exchange(
                url("/api/v1/quantities/compare"),
                HttpMethod.POST,
                jsonEntity(input, authHeaders),
                QuantityMeasurementDTO.class);

        assertThat(compareResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(compareResponse.getBody()).isNotNull();
        assertThat(compareResponse.getBody().getResultString()).isEqualTo("true");
        assertThat(compareResponse.getBody().getOperation()).isEqualTo("compare");

        ResponseEntity<List<QuantityMeasurementDTO>> historyResponse = restTemplate.exchange(
                url("/api/v1/quantities/history/operation/COMPARE"),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                new ParameterizedTypeReference<>() {
                });

        assertThat(historyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(historyResponse.getBody()).isNotEmpty();
    }

    @Test
    void shouldAddQuantities() {
        HttpHeaders authHeaders = authHeaders();
        QuantityInputDTO input = input(1.0, "FEET", MeasurementType.LengthUnit, 12.0, "INCHES", MeasurementType.LengthUnit);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
                url("/api/v1/quantities/add"),
                HttpMethod.POST,
                jsonEntity(input, authHeaders),
                QuantityMeasurementDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResultValue()).isEqualTo(2.0);
        assertThat(response.getBody().getResultUnit()).isEqualTo("FEET");
        assertThat(response.getBody().getOperation()).isEqualTo("add");
    }

    @Test
    void shouldConvertQuantities() {
        HttpHeaders authHeaders = authHeaders();
        QuantityInputDTO input = input(1.0, "FEET", MeasurementType.LengthUnit, 0.0, "INCHES", MeasurementType.LengthUnit);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
                url("/api/v1/quantities/convert"),
                HttpMethod.POST,
                jsonEntity(input, authHeaders),
                QuantityMeasurementDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResultValue()).isEqualTo(12.0);
        assertThat(response.getBody().getResultUnit()).isEqualTo("INCHES");
        assertThat(response.getBody().getOperation()).isEqualTo("convert");
    }

    @Test
    void shouldReturnBadRequestForDifferentMeasurementTypes() {
        HttpHeaders authHeaders = authHeaders();
        QuantityInputDTO input = input(1.0, "FEET", MeasurementType.LengthUnit, 1.0, "KILOGRAM", MeasurementType.WeightUnit);

        ResponseEntity<Map> response = restTemplate.exchange(
                url("/api/v1/quantities/add"),
                HttpMethod.POST,
                jsonEntity(input, authHeaders),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Quantity Measurement Error");
    }

    @Test
    void shouldReturnServerErrorForDivideByZero() {
        HttpHeaders authHeaders = authHeaders();
        QuantityInputDTO input = input(1.0, "FEET", MeasurementType.LengthUnit, 0.0, "INCHES", MeasurementType.LengthUnit);

        ResponseEntity<Map> response = restTemplate.exchange(
                url("/api/v1/quantities/divide"),
                HttpMethod.POST,
                jsonEntity(input, authHeaders),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("message")).isEqualTo("Divide by zero");
    }

    @Test
    void shouldExposeActuatorHealthAndSwagger() {
        ResponseEntity<Map> health = restTemplate.getForEntity(url("/actuator/health"), Map.class);
        ResponseEntity<Map> metrics = restTemplate.getForEntity(url("/actuator/metrics"), Map.class);
        ResponseEntity<Map> apiDocs = restTemplate.getForEntity(url("/api-docs"), Map.class);
        ResponseEntity<String> swagger = restTemplate.getForEntity(url("/swagger-ui.html"), String.class);

        assertThat(health.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(metrics.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(apiDocs.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(swagger.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnErrorHistoryAndOperationCount() {
        HttpHeaders authHeaders = authHeaders();
        QuantityInputDTO badInput = input(1.0, "FEET", MeasurementType.LengthUnit, 1.0, "KILOGRAM", MeasurementType.WeightUnit);
        QuantityInputDTO goodInput = input(1.0, "FEET", MeasurementType.LengthUnit, 12.0, "INCHES", MeasurementType.LengthUnit);

        restTemplate.exchange(url("/api/v1/quantities/add"), HttpMethod.POST, jsonEntity(badInput, authHeaders), Map.class);
        restTemplate.exchange(url("/api/v1/quantities/compare"), HttpMethod.POST, jsonEntity(goodInput, authHeaders), QuantityMeasurementDTO.class);

        ResponseEntity<List<QuantityMeasurementDTO>> errored = restTemplate.exchange(
                url("/api/v1/quantities/history/errored"),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                new ParameterizedTypeReference<>() {
                });
        ResponseEntity<Map> count = restTemplate.exchange(
                url("/api/v1/quantities/count/COMPARE"),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                Map.class);

        assertThat(errored.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(errored.getBody()).isNotEmpty();
        assertThat(errored.getBody().get(0).isError()).isTrue();
        assertThat(count.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(count.getBody()).isNotNull();
        assertThat(count.getBody().get("operation")).isEqualTo("COMPARE");
    }

    @Test
    void shouldReturnOnlyCurrentUsersHistory() {
        HttpHeaders firstUserHeaders = authHeaders();
        HttpHeaders secondUserHeaders = authHeaders();
        QuantityInputDTO input = input(1.0, "FEET", MeasurementType.LengthUnit, 12.0, "INCHES", MeasurementType.LengthUnit);

        restTemplate.exchange(
                url("/api/v1/quantities/compare"),
                HttpMethod.POST,
                jsonEntity(input, firstUserHeaders),
                QuantityMeasurementDTO.class);

        ResponseEntity<List<QuantityMeasurementDTO>> firstUserHistory = restTemplate.exchange(
                url("/api/v1/quantities/history/operation/COMPARE"),
                HttpMethod.GET,
                new HttpEntity<>(firstUserHeaders),
                new ParameterizedTypeReference<>() {
                });

        ResponseEntity<List<QuantityMeasurementDTO>> secondUserHistory = restTemplate.exchange(
                url("/api/v1/quantities/history/operation/COMPARE"),
                HttpMethod.GET,
                new HttpEntity<>(secondUserHeaders),
                new ParameterizedTypeReference<>() {
                });

        assertThat(firstUserHistory.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(firstUserHistory.getBody()).isNotEmpty();
        assertThat(secondUserHistory.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(secondUserHistory.getBody()).isEmpty();
    }

    @Test
    void shouldSupportXmlContentNegotiation() {
        HttpHeaders authHeaders = authHeaders();
        authHeaders.setAccept(List.of(MediaType.APPLICATION_XML));
        QuantityInputDTO input = input(1.0, "FEET", MeasurementType.LengthUnit, 12.0, "INCHES", MeasurementType.LengthUnit);
        HttpEntity<QuantityInputDTO> entity = jsonEntity(input, authHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                url("/api/v1/quantities/compare"),
                HttpMethod.POST,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isNotNull();
        assertThat(response.getHeaders().getContentType().toString()).contains("xml");
        assertThat(response.getBody()).contains("<operation>compare</operation>");
    }

    private HttpEntity<QuantityInputDTO> jsonEntity(QuantityInputDTO input, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(input, headers);
    }

    private HttpHeaders authHeaders() {
        String username = "user_" + UUID.randomUUID();
        Map<String, String> request = Map.of(
                "username", username,
                "password", "Password@123"
        );

        ResponseEntity<AuthResponse> registerResponse =
                restTemplate.postForEntity(url("/api/v1/auth/register"), request, AuthResponse.class);

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(registerResponse.getBody()).isNotNull();
        assertThat(registerResponse.getBody().getToken()).isNotBlank();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(registerResponse.getBody().getToken());
        return headers;
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private QuantityInputDTO input(double leftValue, String leftUnit, MeasurementType leftType,
                                   double rightValue, String rightUnit, MeasurementType rightType) {
        return new QuantityInputDTO(
                new QuantityDTO(leftValue, leftUnit, leftType),
                new QuantityDTO(rightValue, rightUnit, rightType)
        );
    }
}
