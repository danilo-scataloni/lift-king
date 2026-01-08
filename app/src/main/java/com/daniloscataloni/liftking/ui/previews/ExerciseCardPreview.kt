package com.daniloscataloni.liftking.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daniloscataloni.liftking.entities.Exercise
import com.daniloscataloni.liftking.entities.MuscleGroup
import com.daniloscataloni.liftking.ui.components.ExerciseCard

@Preview
@Composable
fun ExerciseCardPreview() {
    ExerciseCard(
        exercise = Exercise(
            id = 1,
            description = "Supino Reto",
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = MuscleGroup.TRICEPS
        )
    )
}