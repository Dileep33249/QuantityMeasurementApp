package org.quantitymeasurement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quantitymeasurement.controller.QuantityMeasurementController;
import org.quantitymeasurement.exception.QuantityMeasurementException;
import org.quantitymeasurement.model.QuantityDTO;
import org.quantitymeasurement.model.QuantityMeasurementEntity;
import org.quantitymeasurement.repository.IQuantityMeasurementRepository;
import org.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import org.quantitymeasurement.service.IQuantityMeasurementService;
import org.quantitymeasurement.service.QuantityMeasurementServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for UC15 N-Tier Architecture Implementation.
 *
 * Tests cover:
 * - Model layer (QuantityDTO, QuantityModel, QuantityMeasurementEntity)
 * - Service layer (QuantityMeasurementServiceImpl)
 * - Controller layer (QuantityMeasurementController)
 * - Repository layer (QuantityMeasurementCacheRepository)
 * - Integration tests across layers
 * - Layer separation and dependency injection
 */
public class QuantityMeasurementAppTest {

    private IQuantityMeasurementRepository repository;
    private IQuantityMeasurementService service;
    private QuantityMeasurementController controller;

    @BeforeEach
    public void setUp() {
        // Fresh repository for each test
        repository = QuantityMeasurementCacheRepository.getInstance();
        repository.deleteAll();

        // Initialize layers with dependency injection
        service = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    // ==================== MODEL LAYER TESTS ====================

    @Test
    public void testQuantityDTO_Construction_Success() {
        QuantityDTO dto = new QuantityDTO(5.0, "FEET", "LENGTH");
        assertEquals(5.0, dto.getValue());
        assertEquals("FEET", dto.getUnit());
        assertEquals("LENGTH", dto.getMeasurementType());
    }

    @Test
    public void testQuantityDTO_InvalidValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityDTO(Double.NaN, "FEET", "LENGTH"));
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityDTO(Double.POSITIVE_INFINITY, "FEET", "LENGTH"));
    }

    @Test
    public void testQuantityDTO_NullUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityDTO(5.0, null, "LENGTH"));
    }

    @Test
    public void testQuantityDTO_EmptyUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityDTO(5.0, "", "LENGTH"));
    }

    @Test
    public void testQuantityDTO_Equality() {
        QuantityDTO dto1 = new QuantityDTO(5.0, "FEET", "LENGTH");
        QuantityDTO dto2 = new QuantityDTO(5.0, "FEET", "LENGTH");
        assertEquals(dto1, dto2);
    }

    @Test
    public void testQuantityMeasurementEntity_SingleOperand_Construction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                QuantityMeasurementEntity.OperationType.CONVERSION,
                5.0, "FEET", "LENGTH",
                1.52, "METRES", "LENGTH");

        assertEquals("Conversion", entity.getOperationType());
        assertEquals(5.0, entity.getOperand1Value());
        assertEquals("FEET", entity.getOperand1Unit());
        assertEquals(1.52, entity.getResultValue());
        assertEquals("METRES", entity.getResultUnit());
        assertFalse(entity.hasError());
    }

    @Test
    public void testQuantityMeasurementEntity_BinaryOperand_Construction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                QuantityMeasurementEntity.OperationType.ADDITION,
                1.0, "FEET", "LENGTH",
                12.0, "INCHES", "LENGTH",
                2.0, "FEET", "LENGTH");

        assertEquals("Addition", entity.getOperationType());
        assertEquals(1.0, entity.getOperand1Value());
        assertEquals(12.0, entity.getOperand2Value());
        assertEquals(2.0, entity.getResultValue());
        assertFalse(entity.hasError());
    }

    @Test
    public void testQuantityMeasurementEntity_Error_Construction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                QuantityMeasurementEntity.OperationType.COMPARISON,
                1.0, "FEET", "LENGTH",
                "Cannot compare null values");

        assertTrue(entity.hasError());
        assertEquals("Cannot compare null values", entity.getErrorMessage());
        assertEquals("ERROR", entity.getOperationResult());
    }

    // ==================== SERVICE LAYER TESTS ====================

    @Test
    public void testService_Compare_SameUnit_Success() {
        QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(1.0, "FEET", "LENGTH");

        QuantityMeasurementEntity result = service.compare(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals(1.0, result.getResultValue()); // 1.0 means equal
    }

    @Test
    public void testService_Compare_DifferentUnit_Success() {
        QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(12.0, "INCHES", "LENGTH");

        QuantityMeasurementEntity result = service.compare(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals(1.0, result.getResultValue()); // 1.0 means equal
    }

    @Test
    public void testService_Compare_CrossCategory_Error() {
        QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(1.0, "GRAM", "WEIGHT");

        QuantityMeasurementEntity result = service.compare(qty1, qty2);

        assertTrue(result.hasError());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    public void testService_Convert_Success() {
        QuantityDTO qty = new QuantityDTO(1.0, "FEET", "LENGTH");

        QuantityMeasurementEntity result = service.convert(qty, "METRES");

        assertFalse(result.hasError());
        assertEquals("METRES", result.getResultUnit());
        // 1 foot ≈ 0.3048 meters
        assertEquals(0.3, result.getResultValue(), 0.01);
    }

    @Test
    public void testService_Add_Success() {
        QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(12.0, "INCHES", "LENGTH");

        QuantityMeasurementEntity result = service.add(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals("FEET", result.getResultUnit());
        // 1 foot + 1 foot = 2 feet
        assertEquals(2.0, result.getResultValue());
    }

    @Test
    public void testService_Add_Temperature_Error() {
        QuantityDTO qty1 = new QuantityDTO(20.0, "CELSIUS", "TEMPERATURE");
        QuantityDTO qty2 = new QuantityDTO(10.0, "CELSIUS", "TEMPERATURE");

        QuantityMeasurementEntity result = service.add(qty1, qty2);

        assertTrue(result.hasError());
        assertTrue(result.getErrorMessage().contains("not supported"));
    }

    @Test
    public void testService_Subtract_Success() {
        QuantityDTO qty1 = new QuantityDTO(10.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(6.0, "INCHES", "LENGTH");

        QuantityMeasurementEntity result = service.subtract(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals("FEET", result.getResultUnit());
        // 10 feet - 0.5 feet = 9.5 feet
        assertEquals(9.5, result.getResultValue());
    }

    @Test
    public void testService_Divide_Success() {
        QuantityDTO qty1 = new QuantityDTO(10.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(2.0, "FEET", "LENGTH");

        QuantityMeasurementEntity result = service.divide(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals(5.0, result.getResultValue());
    }

    @Test
    public void testService_Divide_ByZero_Error() {
        QuantityDTO qty1 = new QuantityDTO(10.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(0.0, "FEET", "LENGTH");

        QuantityMeasurementEntity result = service.divide(qty1, qty2);

        assertTrue(result.hasError());
        assertTrue(result.getErrorMessage().contains("zero"));
    }

    // ==================== CONTROLLER LAYER TESTS ====================

    @Test
    public void testController_PerformCompare_Success() {
        QuantityDTO qty1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO qty2 = new QuantityDTO(1000.0, "GRAM", "WEIGHT");

        QuantityMeasurementEntity result = controller.performCompare(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals(1.0, result.getResultValue());
    }

    @Test
    public void testController_PerformConvert_Success() {
        QuantityDTO qty = new QuantityDTO(1.0, "LITRE", "VOLUME");

        QuantityMeasurementEntity result = controller.performConvert(qty, "MILLILITRE");

        assertFalse(result.hasError());
        assertEquals(1000.0, result.getResultValue());
    }

    @Test
    public void testController_PerformAdd_Success() {
        QuantityDTO qty1 = new QuantityDTO(10.0, "KILOGRAM", "WEIGHT");
        QuantityDTO qty2 = new QuantityDTO(5000.0, "GRAM", "WEIGHT");

        QuantityMeasurementEntity result = controller.performAdd(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals(15.0, result.getResultValue());
    }

    @Test
    public void testController_PerformSubtract_Success() {
        QuantityDTO qty1 = new QuantityDTO(5.0, "LITRE", "VOLUME");
        QuantityDTO qty2 = new QuantityDTO(2.0, "LITRE", "VOLUME");

        QuantityMeasurementEntity result = controller.performSubtract(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals(3.0, result.getResultValue());
    }

    @Test
    public void testController_PerformDivide_Success() {
        QuantityDTO qty1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO qty2 = new QuantityDTO(500.0, "GRAM", "WEIGHT");

        QuantityMeasurementEntity result = controller.performDivide(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals(2.0, result.getResultValue());
    }

    @Test
    public void testController_NullInput_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.performCompare(null, new QuantityDTO(1.0, "FEET", "LENGTH")));
    }

    // ==================== REPOSITORY LAYER TESTS ====================

    @Test
    public void testRepository_Save_Success() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                QuantityMeasurementEntity.OperationType.CONVERSION,
                5.0, "FEET", "LENGTH",
                1.52, "METRES", "LENGTH");

        repository.save(entity);

        assertEquals(1, repository.count());
    }

    @Test
    public void testRepository_FindById_Success() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                QuantityMeasurementEntity.OperationType.ADDITION,
                1.0, "FEET", "LENGTH",
                12.0, "INCHES", "LENGTH",
                2.0, "FEET", "LENGTH");
        repository.save(entity);

        QuantityMeasurementEntity retrieved = repository.findById(0);

        assertNotNull(retrieved);
        assertEquals(entity, retrieved);
    }

    @Test
    public void testRepository_FindAll_Success() {
        QuantityMeasurementEntity entity1 = new QuantityMeasurementEntity(
                QuantityMeasurementEntity.OperationType.CONVERSION,
                5.0, "FEET", "LENGTH",
                1.52, "METRES", "LENGTH");
        QuantityMeasurementEntity entity2 = new QuantityMeasurementEntity(
                QuantityMeasurementEntity.OperationType.CONVERSION,
                1.0, "LITRE", "VOLUME",
                1000.0, "MILLILITRE", "VOLUME");

        repository.save(entity1);
        repository.save(entity2);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    public void testRepository_DeleteAll_Success() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                QuantityMeasurementEntity.OperationType.CONVERSION,
                5.0, "FEET", "LENGTH",
                1.52, "METRES", "LENGTH");
        repository.save(entity);
        assertEquals(1, repository.count());

        repository.deleteAll();

        assertEquals(0, repository.count());
    }

    // ==================== LAYER SEPARATION & INTEGRATION TESTS
    // ====================

    @Test
    public void testLayerSeparation_ServiceIndependence() {
        // Service should work independently of controller
        QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(12.0, "INCHES", "LENGTH");

        QuantityMeasurementEntity result = service.compare(qty1, qty2);

        assertNotNull(result);
        assertFalse(result.hasError());
    }

    @Test
    public void testLayerSeparation_DependencyInjection() {
        // Controller should work with any service implementation
        IQuantityMeasurementService mockService = new QuantityMeasurementServiceImpl(repository);
        QuantityMeasurementController testController = new QuantityMeasurementController(mockService);

        QuantityDTO qty = new QuantityDTO(5.0, "FEET", "LENGTH");
        QuantityMeasurementEntity result = testController.performConvert(qty, "METRES");

        assertNotNull(result);
    }

    @Test
    public void testIntegration_EndToEnd_LengthAddition() {
        // Full integration test: Input → Controller → Service → Repository → Output
        QuantityDTO qty1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO qty2 = new QuantityDTO(12.0, "INCHES", "LENGTH");

        QuantityMeasurementEntity result = controller.performAdd(qty1, qty2);

        assertFalse(result.hasError());
        assertEquals(2.0, result.getResultValue());
        assertEquals(1, repository.count()); // Entity saved to repository
    }

    @Test
    public void testIntegration_EndToEnd_ErrorHandling() {
        // Test error propagation through all layers
        QuantityDTO qty1 = new QuantityDTO(20.0, "CELSIUS", "TEMPERATURE");
        QuantityDTO qty2 = new QuantityDTO(10.0, "CELSIUS", "TEMPERATURE");

        QuantityMeasurementEntity result = controller.performAdd(qty1, qty2);

        assertTrue(result.hasError());
        assertEquals(1, repository.count()); // Error entity saved to repository
    }

    @Test
    public void testBackwardCompatibility_AllUC1_UC14_Operations() {
        // Verify all original operations still work with new architecture

        // Length operations
        QuantityDTO length1 = new QuantityDTO(5.0, "FEET", "LENGTH");
        QuantityDTO length2 = new QuantityDTO(10.0, "INCHES", "LENGTH");
        assertFalse(service.compare(length1, length2).hasError());

        // Weight operations
        QuantityDTO weight1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO weight2 = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        assertFalse(service.add(weight1, weight2).hasError());

        // Volume operations
        QuantityDTO volume1 = new QuantityDTO(1.0, "LITRE", "VOLUME");
        QuantityDTO volume2 = new QuantityDTO(500.0, "MILLILITRE", "VOLUME");
        assertFalse(service.divide(volume1, volume2).hasError());

        // Temperature conversion
        QuantityDTO temp = new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE");
        assertFalse(service.convert(temp, "FAHRENHEIT").hasError());
    }

    @Test
    public void testSingleton_RepositoryInstance() {
        IQuantityMeasurementRepository repo1 = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementRepository repo2 = QuantityMeasurementCacheRepository.getInstance();

        assertSame(repo1, repo2); // Same instance
    }

    @Test
    public void testControlllerInitialization_NullService() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementController(null));
    }

    @Test
    public void testServiceInitialization_NullRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementServiceImpl(null));
    }
}
