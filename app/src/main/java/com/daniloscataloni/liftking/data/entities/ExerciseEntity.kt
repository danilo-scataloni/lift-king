package com.daniloscataloni.liftking.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.domain.models.WeightUnit

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: MuscleGroup?,
    val weightUnit: WeightUnit = WeightUnit.KG,
)
