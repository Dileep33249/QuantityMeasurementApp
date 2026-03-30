package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.exception.GlobalExceptionHandler;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.MeasurementType;
import com.app.quantitymeasurement.security.JwtService;
import com.app.quantitymeasurement.service.AppUserDetailsService;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQuantityMeasurementService service;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @Test
    void shouldCompareQuantities() throws Exception {
        QuantityMeasurementDTO response = new QuantityMeasurementDTO();
        response.setOperation("compare");
        response.setResultString("true");

        Mockito.when(service.compareQuantities(Mockito.any(), Mockito.any())).thenReturn(response);

        String body = """
                {
                  "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
                  "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.operation").value("compare"))
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    void shouldReturnValidationErrorForInvalidUnit() throws Exception {
        String body = """
                {
                  "thisQuantityDTO": {"value": 1.0, "unit": "FOOT", "measurementType": "LengthUnit"},
                  "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
    }

    @Test
    void shouldReturnQuantityError() throws Exception {
        Mockito.when(service.addQuantities(Mockito.any(), Mockito.any()))
                .thenThrow(new QuantityMeasurementException("add Error: Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit"));

        String body = """
                {
                  "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
                  "thatQuantityDTO": {"value": 1.0, "unit": "KILOGRAM", "measurementType": "WeightUnit"}
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("add Error: Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit"));
    }

    @Test
    void shouldReturnOperationHistory() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setOperation("compare");
        dto.setThisMeasurementType(MeasurementType.LengthUnit.name());
        Mockito.when(service.getOperationHistory("COMPARE")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/quantities/history/operation/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operation").value("compare"));
    }

    @Test
    void shouldReturnOperationCount() throws Exception {
        Mockito.when(service.getOperationCount("COMPARE")).thenReturn(3L);

        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("COMPARE"))
                .andExpect(jsonPath("$.count").value(3));
    }

    @Test
    void shouldSupportXmlResponse() throws Exception {
        QuantityMeasurementDTO response = new QuantityMeasurementDTO();
        response.setOperation("compare");
        response.setResultString("true");

        Mockito.when(service.compareQuantities(Mockito.any(), Mockito.any())).thenReturn(response);

        String body = """
                {
                  "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
                  "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_XML)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<operation>compare</operation>")));
    }
}
