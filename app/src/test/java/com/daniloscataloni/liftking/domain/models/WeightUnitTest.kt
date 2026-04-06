package com.daniloscataloni.liftking.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WeightUnitTest {

    @Test
    fun `KG has correct labels`() {
        assertEquals("Quilogramas", WeightUnit.KG.displayLabel)
        assertEquals("kg", WeightUnit.KG.shortLabel)
    }

    @Test
    fun `LIBRAS has correct labels`() {
        assertEquals("Libras", WeightUnit.LIBRAS.displayLabel)
        assertEquals("lb", WeightUnit.LIBRAS.shortLabel)
    }

    @Test
    fun `TIJOLINHOS has correct labels`() {
        assertEquals("Tijolinhos", WeightUnit.TIJOLINHOS.displayLabel)
        assertEquals("tij", WeightUnit.TIJOLINHOS.shortLabel)
    }

    @Test
    fun `enum has exactly three entries`() {
        assertEquals(3, WeightUnit.entries.size)
    }
}
