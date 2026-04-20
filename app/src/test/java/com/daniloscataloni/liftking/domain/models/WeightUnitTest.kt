package com.daniloscataloni.liftking.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WeightUnitTest {

    @Test
    fun `enum keeps supported order stable`() {
        assertEquals(
            listOf(WeightUnit.KG, WeightUnit.LIBRAS, WeightUnit.TIJOLINHOS),
            WeightUnit.entries
        )
    }

    @Test
    fun `enum has exactly three entries`() {
        assertEquals(3, WeightUnit.entries.size)
    }
}
