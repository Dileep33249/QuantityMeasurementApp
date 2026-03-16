package org.quantitymeasurement;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.quantitymeasurement.LengthUnit.*;
import static org.quantitymeasurement.WeightUnit.*;
import static org.quantitymeasurement.VolumeUnit.*;

class QuantityTest {

    @Test
    @DisplayName("ADD enum: 7 + 3 = 10")
    void testEnumConstant_ADD_CorrectlyAdds() {
        assertEquals(10.0, ArithmeticOperation.ADD.compute(7, 3), 1e-9);
    }

    @Test
    @DisplayName("SUBTRACT enum: 7 - 3 = 4")
    void testEnumConstant_SUBTRACT_CorrectlySubtracts() {
        assertEquals(4.0, ArithmeticOperation.SUBTRACT.compute(7, 3), 1e-9);
    }

    @Test
    @DisplayName("DIVIDE enum: 7 / 2 = 3.5")
    void testEnumConstant_DIVIDE_CorrectlyDivides() {
        assertEquals(3.5, ArithmeticOperation.DIVIDE.compute(7, 2), 1e-9);
    }

    @Test
    @DisplayName("DIVIDE enum: divide by zero throws ArithmeticException")
    void testArithmeticOperation_DivideByZero_EnumThrows() {
        assertThrows(ArithmeticException.class, () -> ArithmeticOperation.DIVIDE.compute(10, 0));
    }

    @Test
    @DisplayName("MULTIPLY enum: 4 * 5 = 20")
    void testEnumConstant_MULTIPLY_CorrectlyMultiplies() {
        assertEquals(20.0, ArithmeticOperation.MULTIPLY.compute(4, 5), 1e-9);
    }

    @Test
    @DisplayName("ADD enum: 10 + 5 = 15")
    void testArithmeticOperation_Add_EnumComputation() {
        assertEquals(15.0, ArithmeticOperation.ADD.compute(10, 5), 1e-9);
    }

    @Test
    @DisplayName("SUBTRACT enum: 10 - 5 = 5")
    void testArithmeticOperation_Subtract_EnumComputation() {
        assertEquals(5.0, ArithmeticOperation.SUBTRACT.compute(10, 5), 1e-9);
    }

    @Test
    @DisplayName("DIVIDE enum: 10 / 5 = 2")
    void testArithmeticOperation_Divide_EnumComputation() {
        assertEquals(2.0, ArithmeticOperation.DIVIDE.compute(10, 5), 1e-9);
    }

    @Test
    @DisplayName("add(null) throws IllegalArgumentException")
    void testValidation_NullOperand_Add() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, FEET);
        assertThrows(IllegalArgumentException.class, () -> q.add(null));
    }

    @Test
    @DisplayName("subtract(null) throws IllegalArgumentException")
    void testValidation_NullOperand_Subtract() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, FEET);
        assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
    }

    @Test
    @DisplayName("divide(null) throws IllegalArgumentException")
    void testValidation_NullOperand_Divide() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, FEET);
        assertThrows(IllegalArgumentException.class, () -> q.divide(null));
    }

    @Test
    @DisplayName("All three operations produce the same error message for null operand")
    void testValidation_NullOperand_ConsistentAcrossOperations() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, FEET);
        String addMsg      = catchMessage(() -> q.add(null));
        String subtractMsg = catchMessage(() -> q.subtract(null));
        String divideMsg   = catchMessage(() -> q.divide(null));
        assertEquals(addMsg, subtractMsg);
        assertEquals(subtractMsg, divideMsg);
    }

    @Test
    @DisplayName("add: cross-category throws IllegalArgumentException")
    void testValidation_CrossCategory_Add() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).add((Quantity<LengthUnit>)(Quantity<?>)new Quantity<>(5.0, KILOGRAM)));
    }

    @Test
    @DisplayName("subtract: cross-category throws IllegalArgumentException")
    void testValidation_CrossCategory_Subtract() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).subtract((Quantity<LengthUnit>)(Quantity<?>)new Quantity<>(5.0, KILOGRAM)));
    }

    @Test
    @DisplayName("divide: cross-category throws IllegalArgumentException")
    void testValidation_CrossCategory_Divide() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).divide((Quantity<LengthUnit>)(Quantity<?>)new Quantity<>(5.0, KILOGRAM)));
    }

    @Test
    @DisplayName("All three operations produce the same error message for cross-category")
    @SuppressWarnings("unchecked")
    void testValidation_CrossCategory_ConsistentAcrossOperations() {
        Quantity<LengthUnit> feet = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> kgAsLength = (Quantity<LengthUnit>)(Quantity<?>) new Quantity<>(5.0, KILOGRAM);
        String addMsg      = catchMessage(() -> feet.add(kgAsLength));
        String subtractMsg = catchMessage(() -> feet.subtract(kgAsLength));
        String divideMsg   = catchMessage(() -> feet.divide(kgAsLength));
        assertEquals(addMsg, subtractMsg);
        assertEquals(subtractMsg, divideMsg);
    }

    @Test
    @DisplayName("NaN value in this-operand throws on add")
    void testValidation_NaN_ThisOperand_Add() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, FEET).add(new Quantity<>(1.0, FEET)));
    }

    @Test
    @DisplayName("Infinite value in other-operand throws on subtract")
    void testValidation_Infinite_OtherOperand_Subtract() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(5.0, FEET)
                        .subtract(new Quantity<>(Double.POSITIVE_INFINITY, FEET)));
    }

    @Test
    @DisplayName("add with explicit null target unit throws IllegalArgumentException")
    void testValidation_NullTargetUnit_Add() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).add(new Quantity<>(5.0, INCHES), null));
    }

    @Test
    @DisplayName("subtract with explicit null target unit throws IllegalArgumentException")
    void testValidation_NullTargetUnit_Subtract() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).subtract(new Quantity<>(5.0, INCHES), null));
    }

    @Test
    @DisplayName("1.0 FEET + 12.0 INCHES = 2.0 FEET")
    void testAdd_ImplicitTarget_FeetPlusInches() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCHES));
        assertEquals(2.0, result.getValue(), 1e-9);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    @DisplayName("10.0 KILOGRAM + 5000.0 GRAM → GRAM = 15000.0")
    void testAdd_ExplicitTarget_KgPlusGram() {
        Quantity<WeightUnit> result = new Quantity<>(10.0, KILOGRAM).add(new Quantity<>(5000.0, GRAM), GRAM);
        assertEquals(15000.0, result.getValue(), 1e-9);
        assertEquals(GRAM, result.getUnit());
    }

    @Test
    @DisplayName("1.0 LITRE + 500.0 MILLILITRE → MILLILITRE = 1500.0")
    void testAdd_Volume_LitrePlusMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, LITRE).add(new Quantity<>(500.0, MILLILITRE), MILLILITRE);
        assertEquals(1500.0, result.getValue(), 1e-9);
        assertEquals(MILLILITRE, result.getUnit());
    }

    @Test
    @DisplayName("10.0 FEET - 6.0 INCHES = 9.5 FEET")
    void testSubtract_ImplicitTarget_FeetMinusInches() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCHES));
        assertEquals(9.5, result.getValue(), 1e-9);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    @DisplayName("5.0 LITRE - 2.0 LITRE → MILLILITRE = 3000.0")
    void testSubtract_ExplicitTarget_LitreMinusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, LITRE).subtract(new Quantity<>(2.0, LITRE), MILLILITRE);
        assertEquals(3000.0, result.getValue(), 1e-9);
        assertEquals(MILLILITRE, result.getUnit());
    }

    @Test
    @DisplayName("Subtraction is non-commutative")
    void testSubtract_NonCommutativity() {
        double aMinusB = new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCHES)).getValue();
        double bMinusA = new Quantity<>(6.0, INCHES).subtract(new Quantity<>(10.0, FEET)).getValue();
        assertNotEquals(aMinusB, bMinusA, 1e-9);
    }

    @Test
    @DisplayName("10.0 FEET / 2.0 FEET = 5.0")
    void testDivide_SameUnit() {
        assertEquals(5.0, new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET)), 1e-9);
    }

    @Test
    @DisplayName("24.0 INCHES / 2.0 FEET = 1.0")
    void testDivide_CrossUnit_SameCategory() {
        assertEquals(1.0, new Quantity<>(24.0, INCHES).divide(new Quantity<>(2.0, FEET)), 1e-9);
    }

    @Test
    @DisplayName("1.0 KILOGRAM / 500.0 GRAM = 2.0")
    void testDivide_Weight_KgByGram() {
        assertEquals(2.0, new Quantity<>(1.0, KILOGRAM).divide(new Quantity<>(500.0, GRAM)), 1e-9);
    }

    @Test
    @DisplayName("Division by zero throws ArithmeticException")
    void testDivide_ByZero_ThrowsArithmeticException() {
        assertThrows(ArithmeticException.class,
                () -> new Quantity<>(10.0, FEET).divide(new Quantity<>(0.0, FEET)));
    }

    @Test
    @DisplayName("Division is non-commutative")
    void testDivide_NonCommutativity() {
        double ab = new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET));
        double ba = new Quantity<>(2.0, FEET).divide(new Quantity<>(10.0, FEET));
        assertNotEquals(ab, ba, 1e-9);
    }

    @Test
    @DisplayName("Add/subtract rounds to two decimal places")
    void testRounding_AddSubtract_TwoDecimalPlaces() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, FEET).add(new Quantity<>(1.0, INCHES), METRES);
        assertEquals(0.33, result.getValue(), 1e-9);
    }

    @Test
    @DisplayName("Division returns raw double without rounding")
    void testRounding_Divide_NoRounding() {
        double result = new Quantity<>(10.0, FEET).divide(new Quantity<>(3.0, FEET));
        assertNotEquals(3.33, result, 1e-9);
        assertTrue(result > 3.333);
    }

    @Test
    @DisplayName("Implicit target unit defaults to first operand's unit")
    void testImplicitTargetUnit_Add() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCHES));
        assertEquals(FEET, result.getUnit());
    }

    @Test
    @DisplayName("Explicit target unit overrides first operand's unit")
    void testExplicitTargetUnit_Overrides() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCHES), INCHES);
        assertEquals(INCHES, result.getUnit());
        assertEquals(24.0, result.getValue(), 1e-9);
    }

    @Test
    @DisplayName("Original quantities unchanged after add")
    void testImmutability_AfterAdd() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, INCHES);
        q1.add(q2);
        assertEquals(1.0, q1.getValue(), 1e-9);
        assertEquals(FEET, q1.getUnit());
        assertEquals(12.0, q2.getValue(), 1e-9);
        assertEquals(INCHES, q2.getUnit());
    }

    @Test
    @DisplayName("Original quantities unchanged after subtract")
    void testImmutability_AfterSubtract() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, INCHES);
        q1.subtract(q2);
        assertEquals(10.0, q1.getValue(), 1e-9);
        assertEquals(6.0, q2.getValue(), 1e-9);
    }

    @Test
    @DisplayName("Original quantities unchanged after divide")
    void testImmutability_AfterDivide() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, FEET);
        q1.divide(q2);
        assertEquals(10.0, q1.getValue(), 1e-9);
        assertEquals(2.0, q2.getValue(), 1e-9);
    }

    @Test
    @DisplayName("Add/subtract/divide work across Length, Weight, and Volume")
    void testAllOperations_AcrossAllCategories() {
        assertDoesNotThrow(() -> new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCHES)));
        assertDoesNotThrow(() -> new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCHES)));
        assertDoesNotThrow(() -> new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET)));

        assertDoesNotThrow(() -> new Quantity<>(1.0, KILOGRAM).add(new Quantity<>(500.0, GRAM)));
        assertDoesNotThrow(() -> new Quantity<>(1.0, KILOGRAM).subtract(new Quantity<>(500.0, GRAM)));
        assertDoesNotThrow(() -> new Quantity<>(1.0, KILOGRAM).divide(new Quantity<>(500.0, GRAM)));

        assertDoesNotThrow(() -> new Quantity<>(1.0, LITRE).add(new Quantity<>(500.0, MILLILITRE)));
        assertDoesNotThrow(() -> new Quantity<>(1.0, LITRE).subtract(new Quantity<>(500.0, MILLILITRE)));
        assertDoesNotThrow(() -> new Quantity<>(1.0, LITRE).divide(new Quantity<>(500.0, MILLILITRE)));
    }

    @Test
    @DisplayName("performBaseArithmetic is a private method")
    void testHelper_PrivateVisibility_performBaseArithmetic() throws Exception {
        java.lang.reflect.Method m = Quantity.class.getDeclaredMethod(
                "performBaseArithmetic", Quantity.class, ArithmeticOperation.class);
        assertTrue(java.lang.reflect.Modifier.isPrivate(m.getModifiers()));
    }

    @Test
    @DisplayName("validateArithmeticOperands is a private method")
    void testValidation_Helper_PrivateVisibility() throws Exception {
        // Find the method by name since generic types are erased at runtime
        java.lang.reflect.Method m = null;
        for (java.lang.reflect.Method method : Quantity.class.getDeclaredMethods()) {
            if (method.getName().equals("validateArithmeticOperands")) {
                m = method;
                break;
            }
        }
        assertNotNull(m, "validateArithmeticOperands method should exist");
        assertTrue(java.lang.reflect.Modifier.isPrivate(m.getModifiers()));
    }

    @Test
    @DisplayName("Chain: add then subtract produces correct result")
    void testArithmetic_ChainAddSubtract() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, FEET)
                .add(new Quantity<>(12.0, INCHES))
                .subtract(new Quantity<>(6.0, INCHES));
        assertEquals(1.5, result.getValue(), 1e-9);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    @DisplayName("Chain: add then divide produces correct scalar")
    void testArithmetic_ChainAddThenDivide() {
        double result = new Quantity<>(1.0, FEET)
                .add(new Quantity<>(12.0, INCHES))
                .divide(new Quantity<>(2.0, FEET));
        assertEquals(1.0, result, 1e-9);
    }

    private String catchMessage(Runnable r) {
        try {
            r.run();
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}