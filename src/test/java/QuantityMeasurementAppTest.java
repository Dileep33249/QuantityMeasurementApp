import org.quantitymeasurement.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    @Test
    @DisplayName("1 foot equals 1 foot")
    void testEquality_FootToFoot_SameValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    @DisplayName("1 foot does not equal 2 feet")
    void testEquality_FootToFoot_DifferentValue() {
        assertFalse(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(2.0, LengthUnit.FEET)));
    }

    @Test
    @DisplayName("1 foot equals 12 inches")
    void testEquality_FootToInch_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCH)));
    }

    @Test
    @DisplayName("12 inches equals 1 foot (symmetric)")
    void testEquality_InchToFoot_Symmetric() {
        assertTrue(new Quantity<>(12.0, LengthUnit.INCH).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    @DisplayName("1 yard equals 3 feet")
    void testEquality_YardToFeet_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.YARD).equals(new Quantity<>(3.0, LengthUnit.FEET)));
    }

    @Test
    @DisplayName("Convert 1 foot to 12 inches")
    void testConversion_FootToInch() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCH);
        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    @Test
    @DisplayName("Convert 1 yard to 3 feet")
    void testConversion_YardToFeet() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.YARD).convertTo(LengthUnit.FEET);
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("1 foot + 12 inches = 2 feet")
    void testAddition_FootPlusInches_InFeet() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET)
                .add(new Quantity<>(12.0, LengthUnit.INCH));
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    @DisplayName("1 yard + 3 feet = 2 yards")
    void testAddition_YardPlusFeet_InYards() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.YARD)
                .add(new Quantity<>(3.0, LengthUnit.FEET), LengthUnit.YARD);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.YARD, result.getUnit());
    }

    @Test
    @DisplayName("1 kg equals 1 kg")
    void testEquality_KgToKg_SameValue() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    @DisplayName("1 kg equals 1000 grams")
    void testEquality_KgToGram_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    @DisplayName("1000 grams equals 1 kg (symmetric)")
    void testEquality_GramToKg_Symmetric() {
        assertTrue(new Quantity<>(1000.0, WeightUnit.GRAM).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    @DisplayName("1 kg does NOT equal 1 pound")
    void testEquality_KgToPound_NotEqual() {
        assertFalse(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1.0, WeightUnit.POUND)));
    }

    @Test
    @DisplayName("Convert 1 kg to 1000 grams")
    void testConversion_KgToGram() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
        assertEquals(1000.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("Convert 1 pound to ~0.453592 kg")
    void testConversion_PoundToKg() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.POUND).convertTo(WeightUnit.KILOGRAM);
        assertEquals(0.453592, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("1 kg + 1000 g = 2 kg")
    void testAddition_KgPlusGram_InKg() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .add(new Quantity<>(1000.0, WeightUnit.GRAM));
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    @DisplayName("1 kg + 1 pound in grams")
    void testAddition_KgPlusPound_InGrams() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM)
                .add(new Quantity<>(1.0, WeightUnit.POUND), WeightUnit.GRAM);
        assertEquals(1453.592, result.getValue(), 0.001);
    }

    @Test
    @DisplayName("VolumeUnit.LITRE has conversionFactor 1.0")
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPSILON);
    }

    @Test
    @DisplayName("VolumeUnit.MILLILITRE has conversionFactor 0.001")
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON);
    }

    @Test
    @DisplayName("VolumeUnit.GALLON has conversionFactor 3.78541")
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON);
    }

    @Test
    @DisplayName("LITRE.convertToBaseUnit(5.0) = 5.0")
    void testConvertToBaseUnit_LitreToLitre() {
        assertEquals(5.0, VolumeUnit.LITRE.convertToBaseUnit(5.0), EPSILON);
    }

    @Test
    @DisplayName("MILLILITRE.convertToBaseUnit(1000.0) = 1.0")
    void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPSILON);
    }

    @Test
    @DisplayName("GALLON.convertToBaseUnit(1.0) = 3.78541")
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    @DisplayName("LITRE.convertFromBaseUnit(2.0) = 2.0")
    void testConvertFromBaseUnit_LitreToLitre() {
        assertEquals(2.0, VolumeUnit.LITRE.convertFromBaseUnit(2.0), EPSILON);
    }

    @Test
    @DisplayName("MILLILITRE.convertFromBaseUnit(1.0) = 1000.0")
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    @DisplayName("GALLON.convertFromBaseUnit(3.78541) = ~1.0")
    void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), EPSILON);
    }

    @Test
    @DisplayName("1 L equals 1 L")
    void testEquality_LitreToLitre_SameValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    @DisplayName("1 L does not equal 2 L")
    void testEquality_LitreToLitre_DifferentValue() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    @DisplayName("1000 mL equals 1000 mL")
    void testEquality_MillilitreToMillilitre_SameValue() {
        assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    @DisplayName("1 gallon equals 1 gallon")
    void testEquality_GallonToGallon_SameValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.GALLON).equals(new Quantity<>(1.0, VolumeUnit.GALLON)));
    }

    @Test
    @DisplayName("1 L equals 1000 mL")
    void testEquality_LitreToMillilitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    @DisplayName("1000 mL equals 1 L (symmetric)")
    void testEquality_MillilitreToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    @DisplayName("3.78541 L equals 1 gallon")
    void testEquality_LitreToGallon_EquivalentValue() {
        assertTrue(new Quantity<>(3.78541, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.GALLON)));
    }

    @Test
    @DisplayName("1 gallon equals 3.78541 L (symmetric)")
    void testEquality_GallonToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.GALLON).equals(new Quantity<>(3.78541, VolumeUnit.LITRE)));
    }

    @Test
    @DisplayName("500 mL equals 0.5 L")
    void testEquality_MillilitreToLitre_HalfLitre() {
        assertTrue(new Quantity<>(500.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(0.5, VolumeUnit.LITRE)));
    }

    @Test
    @DisplayName("0 L equals 0 mL")
    void testEquality_ZeroValue() {
        assertTrue(new Quantity<>(0.0, VolumeUnit.LITRE).equals(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    @DisplayName("-1 L equals -1000 mL")
    void testEquality_NegativeVolume() {
        assertTrue(new Quantity<>(-1.0, VolumeUnit.LITRE).equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    @DisplayName("1 000 000 mL equals 1000 L")
    void testEquality_LargeVolumeValue() {
        assertTrue(new Quantity<>(1_000_000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1000.0, VolumeUnit.LITRE)));
    }

    @Test
    @DisplayName("0.001 L equals 1 mL")
    void testEquality_SmallVolumeValue() {
        assertTrue(new Quantity<>(0.001, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    @DisplayName("Quantity equals itself (reflexive)")
    void testEquality_SameReference() {
        Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(q.equals(q));
    }

    @Test
    @DisplayName("Quantity.equals(null) returns false")
    void testEquality_NullComparison() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
    }

    @Test
    @DisplayName("Null unit throws IllegalArgumentException")
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, (VolumeUnit) null));
    }

    @Test
    @DisplayName("Transitive equality: 1 L = 1000 mL and 1000 mL = 1 L implies 1 L = 1 L")
    void testEquality_TransitiveProperty() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> c = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    @DisplayName("1 L converts to 1000 mL")
    void testConversion_LitreToMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);
        assertEquals(1000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    @DisplayName("1000 mL converts to 1 L")
    void testConversion_MillilitreToLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE);
        assertEquals(1.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    @DisplayName("1 gallon converts to ~3.78541 L")
    void testConversion_GallonToLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE);
        assertEquals(3.78541, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("3.78541 L converts to ~1 gallon")
    void testConversion_LitreToGallon() {
        Quantity<VolumeUnit> result = new Quantity<>(3.78541, VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("1000 mL converts to ~0.264172 gallons")
    void testConversion_MillilitreToGallon() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.GALLON);
        assertEquals(1.0 / 3.78541, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("5 L converts to 5 L (same unit)")
    void testConversion_SameUnit() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE).convertTo(VolumeUnit.LITRE);
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("0 L converts to 0 mL")
    void testConversion_ZeroValue() {
        Quantity<VolumeUnit> result = new Quantity<>(0.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);
        assertEquals(0.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("-1 L converts to -1000 mL")
    void testConversion_NegativeValue() {
        Quantity<VolumeUnit> result = new Quantity<>(-1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);
        assertEquals(-1000.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("Round-trip: 1.5 L → mL → L = 1.5 L")
    void testConversion_RoundTrip() {
        Quantity<VolumeUnit> result = new Quantity<>(1.5, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.LITRE);
        assertEquals(1.5, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("1 L + 2 L = 3 L")
    void testAddition_SameUnit_LitrePlusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(2.0, VolumeUnit.LITRE));
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    @DisplayName("500 mL + 500 mL = 1000 mL")
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(500.0, VolumeUnit.MILLILITRE)
                .add(new Quantity<>(500.0, VolumeUnit.MILLILITRE));
        assertEquals(1000.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("1 L + 1000 mL = 2 L")
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    @DisplayName("1000 mL + 1 L = 2000 mL")
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .add(new Quantity<>(1.0, VolumeUnit.LITRE));
        assertEquals(2000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    @DisplayName("1 gal + 3.78541 L = ~2 gal")
    void testAddition_CrossUnit_GallonPlusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.GALLON)
                .add(new Quantity<>(3.78541, VolumeUnit.LITRE));
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.GALLON, result.getUnit());
    }

    @Test
    @DisplayName("1 L + 1000 mL → mL = 2000 mL")
    void testAddition_ExplicitTargetUnit_Millilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.MILLILITRE);
        assertEquals(2000.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("1 L + 1000 mL → L = 2 L")
    void testAddition_ExplicitTargetUnit_Litre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE);
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("3.78541 L + 3.78541 L → gallon = ~2 gal")
    void testAddition_ExplicitTargetUnit_Gallon() {
        Quantity<VolumeUnit> result = new Quantity<>(3.78541, VolumeUnit.LITRE)
                .add(new Quantity<>(3.78541, VolumeUnit.LITRE), VolumeUnit.GALLON);
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("500 mL + 1 L → gallon = ~0.396258 gal")
    void testAddition_ExplicitTargetUnit_MixedToGallon() {
        Quantity<VolumeUnit> result = new Quantity<>(500.0, VolumeUnit.MILLILITRE)
                .add(new Quantity<>(1.0, VolumeUnit.LITRE), VolumeUnit.GALLON);
        assertEquals(1.5 / 3.78541, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("2 L + 4 gal → L = ~17.14164 L")
    void testAddition_ExplicitTargetUnit_LitrePlusGallonToLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(2.0, VolumeUnit.LITRE)
                .add(new Quantity<>(4.0, VolumeUnit.GALLON), VolumeUnit.LITRE);
        assertEquals(2.0 + 4.0 * 3.78541, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("Commutativity: 1 L + 1000 mL = 1000 mL + 1 L (same target unit)")
    void testAddition_Commutativity() {
        Quantity<VolumeUnit> r1 = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE);
        Quantity<VolumeUnit> r2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .add(new Quantity<>(1.0, VolumeUnit.LITRE), VolumeUnit.LITRE);
        assertEquals(r1.getValue(), r2.getValue(), EPSILON);
    }

    @Test
    @DisplayName("5 L + 0 mL = 5 L (identity)")
    void testAddition_WithZero() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
                .add(new Quantity<>(0.0, VolumeUnit.MILLILITRE));
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("5 L + (-2000 mL) = 3 L")
    void testAddition_NegativeValues() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
                .add(new Quantity<>(-2000.0, VolumeUnit.MILLILITRE));
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("1e6 L + 1e6 L = 2e6 L")
    void testAddition_LargeValues() {
        Quantity<VolumeUnit> result = new Quantity<>(1e6, VolumeUnit.LITRE)
                .add(new Quantity<>(1e6, VolumeUnit.LITRE));
        assertEquals(2e6, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("0.001 L + 0.002 L = ~0.003 L")
    void testAddition_SmallValues() {
        Quantity<VolumeUnit> result = new Quantity<>(0.001, VolumeUnit.LITRE)
                .add(new Quantity<>(0.002, VolumeUnit.LITRE));
        assertEquals(0.003, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("Volume (Litre) != Length (Feet)")
    void testEquality_VolumeVsLength_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    @DisplayName("Volume (Litre) != Weight (Kilogram)")
    void testEquality_VolumeVsWeight_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    @DisplayName("Length (Feet) != Weight (Kilogram)")
    void testEquality_LengthVsWeight_Incompatible() {
        assertFalse(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    @DisplayName("add(null) throws IllegalArgumentException")
    void testAddition_NullOther_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, VolumeUnit.LITRE).add(null));
    }

    @Test
    @DisplayName("convertTo(null) throws IllegalArgumentException")
    void testConversion_NullTargetUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(null));
    }

    @Test
    @DisplayName("add(other, null) throws IllegalArgumentException")
    void testAddition_NullTargetUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(1.0, VolumeUnit.LITRE), null));
    }

    @Test
    @DisplayName("VolumeUnit unit names are correct")
    void testVolumeUnitNames() {
        assertEquals("Litre",      VolumeUnit.LITRE.getUnitName());
        assertEquals("Millilitre", VolumeUnit.MILLILITRE.getUnitName());
        assertEquals("Gallon",     VolumeUnit.GALLON.getUnitName());
    }

    @Test
    @DisplayName("LengthUnit unit names are correct")
    void testLengthUnitNames() {
        assertEquals("Feet",       LengthUnit.FEET.getUnitName());
        assertEquals("Inch",       LengthUnit.INCH.getUnitName());
        assertEquals("Yard",       LengthUnit.YARD.getUnitName());
        assertEquals("Centimetre", LengthUnit.CENTIMETRE.getUnitName());
        assertEquals("Metre",      LengthUnit.METRE.getUnitName());
    }

    @Test
    @DisplayName("WeightUnit unit names are correct")
    void testWeightUnitNames() {
        assertEquals("Kilogram", WeightUnit.KILOGRAM.getUnitName());
        assertEquals("Gram",     WeightUnit.GRAM.getUnitName());
        assertEquals("Pound",    WeightUnit.POUND.getUnitName());
        assertEquals("Tonne",    WeightUnit.TONNE.getUnitName());
    }

    @Test
    @DisplayName("Addition does not modify original quantities")
    void testImmutability_AdditionDoesNotModifyOperands() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(2.0, VolumeUnit.LITRE);
        a.add(b);
        assertEquals(1.0, a.getValue(), EPSILON);
        assertEquals(2.0, b.getValue(), EPSILON);
    }

    @Test
    @DisplayName("Conversion does not modify original quantity")
    void testImmutability_ConversionDoesNotModifyOriginal() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        a.convertTo(VolumeUnit.MILLILITRE);
        assertEquals(1.0, a.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITRE, a.getUnit());
    }

    @Test
    @DisplayName("UC11 volume additions do not affect length (backward compat)")
    void testBackwardCompatibility_LengthUnaffected() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCH)));
    }

    @Test
    @DisplayName("UC11 volume additions do not affect weight (backward compat)")
    void testBackwardCompatibility_WeightUnaffected() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    @DisplayName("Generic Quantity works seamlessly with VolumeUnit")
    void testScalability_VolumeIntegration() {
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(v1.equals(v2));
        assertEquals(1000.0, v1.convertTo(VolumeUnit.MILLILITRE).getValue(), EPSILON);
        assertEquals(2.0, v1.add(v2).getValue(), EPSILON);
    }

    @Test
    @DisplayName("10 ft - 5 ft = 5 ft")
    void testSubtraction_SameUnit_FeetMinusFeet() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(5.0, LengthUnit.FEET));
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    @DisplayName("10 L - 3 L = 7 L")
    void testSubtraction_SameUnit_LitreMinusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(10.0, VolumeUnit.LITRE)
                .subtract(new Quantity<>(3.0, VolumeUnit.LITRE));
        assertEquals(7.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("10 ft - 6 in = 9.5 ft")
    void testSubtraction_CrossUnit_FeetMinusInches() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCH));
        assertEquals(9.5, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    @DisplayName("120 in - 5 ft = 60 in")
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        Quantity<LengthUnit> result = new Quantity<>(120.0, LengthUnit.INCH)
                .subtract(new Quantity<>(5.0, LengthUnit.FEET));
        assertEquals(60.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    @Test
    @DisplayName("10 ft - 6 in → ft = 9.5 ft (explicit)")
    void testSubtraction_ExplicitTargetUnit_Feet() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.FEET);
        assertEquals(9.5, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("10 ft - 6 in → in = 114 in (explicit)")
    void testSubtraction_ExplicitTargetUnit_Inches() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.INCH);
        assertEquals(114.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("5 L - 2 L → mL = 3000 mL (explicit)")
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
                .subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);
        assertEquals(3000.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("5 ft - 10 ft = -5 ft (negative result)")
    void testSubtraction_ResultingInNegative() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .subtract(new Quantity<>(10.0, LengthUnit.FEET));
        assertEquals(-5.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("10 ft - 120 in = 0 ft (zero result)")
    void testSubtraction_ResultingInZero() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(120.0, LengthUnit.INCH));
        assertEquals(0.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("5 ft - 0 in = 5 ft (identity)")
    void testSubtraction_WithZeroOperand() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .subtract(new Quantity<>(0.0, LengthUnit.INCH));
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("5 ft - (-2 ft) = 7 ft (negative operand)")
    void testSubtraction_WithNegativeValues() {
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
                .subtract(new Quantity<>(-2.0, LengthUnit.FEET));
        assertEquals(7.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("Subtraction is non-commutative")
    void testSubtraction_NonCommutative() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        assertEquals(5.0,  a.subtract(b).getValue(), EPSILON);
        assertEquals(-5.0, b.subtract(a).getValue(), EPSILON);
    }

    @Test
    @DisplayName("1e6 kg - 5e5 kg = 5e5 kg (large values)")
    void testSubtraction_WithLargeValues() {
        Quantity<WeightUnit> result = new Quantity<>(1e6, WeightUnit.KILOGRAM)
                .subtract(new Quantity<>(5e5, WeightUnit.KILOGRAM));
        assertEquals(5e5, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("0.001 ft - 0.0005 ft ≈ 0.0005 ft (small values)")
    void testSubtraction_WithSmallValues() {
        Quantity<LengthUnit> result = new Quantity<>(0.001, LengthUnit.FEET)
                .subtract(new Quantity<>(0.0005, LengthUnit.FEET));
        assertEquals(0.0005, result.getValue(), 1e-4);
    }

    @Test
    @DisplayName("subtract(null) throws IllegalArgumentException")
    void testSubtraction_NullOperand() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(null));
    }

    @Test
    @DisplayName("subtract(other, null) throws IllegalArgumentException")
    void testSubtraction_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(5.0, LengthUnit.FEET), null));
    }

    @Test
    @DisplayName("Subtracting different categories throws IllegalArgumentException")
    void testSubtraction_CrossCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET)));
    }

    @Test
    @DisplayName("Subtraction works across all categories")
    void testSubtraction_AllMeasurementCategories() {
        assertEquals(5.0, new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET)).getValue(), EPSILON);
        assertEquals(5.0, new Quantity<>(10.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(5.0, WeightUnit.KILOGRAM)).getValue(), EPSILON);
        assertEquals(5.0, new Quantity<>(10.0, VolumeUnit.LITRE).subtract(new Quantity<>(5.0, VolumeUnit.LITRE)).getValue(), EPSILON);
    }

    @Test
    @DisplayName("Chained subtraction: 10 ft - 2 ft - 1 ft = 7 ft")
    void testSubtraction_ChainedOperations() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                .subtract(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(7.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("Original quantities unchanged after subtraction (immutability)")
    void testSubtraction_Immutability() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        a.subtract(b);
        assertEquals(10.0, a.getValue(), EPSILON);
        assertEquals(5.0, b.getValue(), EPSILON);
    }

    @Test
    @DisplayName("A + B - B ≈ A (addition and subtraction are inverse)")
    void testSubtractionAddition_Inverse() {
        Quantity<LengthUnit> a = new Quantity<>(7.5, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = a.add(b).subtract(b);
        assertEquals(a.getValue(), result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("Subtraction result rounded to two decimal places")
    void testSubtraction_PrecisionAndRounding() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCH));
        assertEquals(9.5, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("10 kg - 5000 g → kg = 5 kg")
    void testSubtraction_CrossUnit_WeightKgMinusGram() {
        Quantity<WeightUnit> result = new Quantity<>(10.0, WeightUnit.KILOGRAM)
                .subtract(new Quantity<>(5000.0, WeightUnit.GRAM));
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("5 L - 500 mL → L = 4.5 L")
    void testSubtraction_CrossUnit_VolumeMinusMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
                .subtract(new Quantity<>(500.0, VolumeUnit.MILLILITRE));
        assertEquals(4.5, result.getValue(), EPSILON);
    }

    @Test
    @DisplayName("10 ft ÷ 2 ft = 5.0")
    void testDivision_SameUnit_FeetDividedByFeet() {
        assertEquals(5.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    @DisplayName("10 L ÷ 5 L = 2.0")
    void testDivision_SameUnit_LitreDividedByLitre() {
        assertEquals(2.0, new Quantity<>(10.0, VolumeUnit.LITRE).divide(new Quantity<>(5.0, VolumeUnit.LITRE)), EPSILON);
    }

    @Test
    @DisplayName("24 in ÷ 2 ft = 1.0")
    void testDivision_CrossUnit_FeetDividedByInches() {
        assertEquals(1.0, new Quantity<>(24.0, LengthUnit.INCH).divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    @DisplayName("2 kg ÷ 2000 g = 1.0")
    void testDivision_CrossUnit_KilogramDividedByGram() {
        assertEquals(1.0, new Quantity<>(2.0, WeightUnit.KILOGRAM).divide(new Quantity<>(2000.0, WeightUnit.GRAM)), EPSILON);
    }

    @Test
    @DisplayName("10 ft ÷ 2 ft = 5.0 (ratio > 1)")
    void testDivision_RatioGreaterThanOne() {
        assertEquals(5.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    @DisplayName("5 ft ÷ 10 ft = 0.5 (ratio < 1)")
    void testDivision_RatioLessThanOne() {
        assertEquals(0.5, new Quantity<>(5.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    @DisplayName("10 ft ÷ 10 ft = 1.0 (ratio = 1)")
    void testDivision_RatioEqualToOne() {
        assertEquals(1.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    @DisplayName("Division is non-commutative")
    void testDivision_NonCommutative() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        assertEquals(2.0, a.divide(b), EPSILON);
        assertEquals(0.5, b.divide(a), EPSILON);
    }

    @Test
    @DisplayName("Division by zero throws ArithmeticException")
    void testDivision_ByZero() {
        assertThrows(ArithmeticException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET)));
    }

    @Test
    @DisplayName("1e6 kg ÷ 1 kg = 1e6 (very large ratio)")
    void testDivision_WithLargeRatio() {
        assertEquals(1e6, new Quantity<>(1e6, WeightUnit.KILOGRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM)), EPSILON);
    }

    @Test
    @DisplayName("1 kg ÷ 1e6 kg = 1e-6 (very small ratio)")
    void testDivision_WithSmallRatio() {
        assertEquals(1e-6, new Quantity<>(1.0, WeightUnit.KILOGRAM).divide(new Quantity<>(1e6, WeightUnit.KILOGRAM)), 1e-12);
    }

    @Test
    @DisplayName("divide(null) throws IllegalArgumentException")
    void testDivision_NullOperand() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).divide(null));
    }

    @Test
    @DisplayName("Dividing different categories throws IllegalArgumentException")
    void testDivision_CrossCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(5.0, WeightUnit.KILOGRAM)));
    }

    @Test
    @DisplayName("Division works across all categories")
    void testDivision_AllMeasurementCategories() {
        assertEquals(2.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(5.0, LengthUnit.FEET)), EPSILON);
        assertEquals(2.0, new Quantity<>(10.0, WeightUnit.KILOGRAM).divide(new Quantity<>(5.0, WeightUnit.KILOGRAM)), EPSILON);
        assertEquals(2.0, new Quantity<>(10.0, VolumeUnit.LITRE).divide(new Quantity<>(5.0, VolumeUnit.LITRE)), EPSILON);
    }

    @Test
    @DisplayName("(A ÷ B) ÷ C ≠ A ÷ (B ÷ C) — division is non-associative")
    void testDivision_Associativity() {
        double a = new Quantity<>(12.0, LengthUnit.FEET).divide(new Quantity<>(4.0, LengthUnit.FEET));
        double bc = new Quantity<>(4.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));
        double ab_c = new Quantity<>(3.0 * 1.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertNotEquals(a / bc, ab_c, EPSILON);
    }

    @Test
    @DisplayName("Subtraction and division can be chained together")
    void testSubtractionAndDivision_Integration() {
        Quantity<LengthUnit> diff   = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(4.0, LengthUnit.FEET));
        double ratio = diff.divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(3.0, ratio, EPSILON);
    }

    @Test
    @DisplayName("Original quantities unchanged after division (immutability)")
    void testDivision_Immutability() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        a.divide(b);
        assertEquals(10.0, a.getValue(), EPSILON);
        assertEquals(5.0, b.getValue(), EPSILON);
    }

    @Test
    @DisplayName("1000 mL ÷ 1 L = 1.0 (cross-unit volume)")
    void testDivision_CrossUnit_MillilitreDividedByLitre() {
        assertEquals(1.0, new Quantity<>(1000.0, VolumeUnit.MILLILITRE).divide(new Quantity<>(1.0, VolumeUnit.LITRE)), EPSILON);
    }

    @Test
    @DisplayName("2 kg ÷ 1 kg = 2.0")
    void testDivision_SameUnit_KilogramDividedByKilogram() {
        assertEquals(2.0, new Quantity<>(2.0, WeightUnit.KILOGRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM)), EPSILON);
    }

    @Test
    @DisplayName("2 kg ÷ 1000 g = 2.0 (cross-unit weight)")
    void testDivision_CrossUnit_GramDividedByKilogram() {
        assertEquals(2.0, new Quantity<>(2000.0, WeightUnit.GRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM)), EPSILON);
    }
}