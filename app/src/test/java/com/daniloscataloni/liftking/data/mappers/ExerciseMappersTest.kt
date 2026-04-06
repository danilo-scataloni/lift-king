package com.daniloscataloni.liftking.data.mappers

import com.daniloscataloni.liftking.data.entities.ExerciseEntity
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.domain.models.WeightUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExerciseMappersTest {

    @Test
    fun `toDomain preserves weightUnit`() {
        val entity = ExerciseEntity(
            id = 1,
            description = "Supino",
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = null,
            weightUnit = WeightUnit.TIJOLINHOS
        )
        val domain = entity.toDomain()
        assertEquals(WeightUnit.TIJOLINHOS, domain.weightUnit)
    }

    @Test
    fun `toEntity preserves weightUnit`() {
        val domain = Exercise(
            id = 1,
            description = "Supino",
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = null,
            weightUnit = WeightUnit.LIBRAS
        )
        val entity = domain.toEntity()
        assertEquals(WeightUnit.LIBRAS, entity.weightUnit)
    }

    @Test
    fun `round-trip preserves all fields including weightUnit`() {
        val original = Exercise(
            id = 5,
            description = "Agachamento",
            primaryMuscleGroup = MuscleGroup.QUADS,
            secondaryMuscleGroups = MuscleGroup.HAMSTRINGS,
            weightUnit = WeightUnit.KG
        )
        val roundTripped = original.toEntity().toDomain()
        assertEquals(original, roundTripped)
    }

    @Test
    fun `default weightUnit is KG when not specified`() {
        val domain = Exercise(
            id = 1,
            description = "Test",
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = null
        )
        assertEquals(WeightUnit.KG, domain.weightUnit)
    }
}
