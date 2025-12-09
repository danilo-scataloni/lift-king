package com.daniloscataloni.liftking.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.daniloscataloni.liftking.entities.MuscleGroup

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: MuscleGroup?,
)
