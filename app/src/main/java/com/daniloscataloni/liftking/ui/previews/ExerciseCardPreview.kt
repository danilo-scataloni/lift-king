package com.daniloscataloni.liftking.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daniloscataloni.liftking.entities.Exercise
import com.daniloscataloni.liftking.entities.MuscleGroup
import com.daniloscataloni.liftking.ui.components.ExerciseCard
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun ExerciseCardPreview() {
    LiftKingTheme {
        ExerciseCard(
            exercise = Exercise(
                id = 1,
                description = "Supino Reto",
                primaryMuscleGroup = MuscleGroup.CHEST,
                secondaryMuscleGroups = MuscleGroup.TRICEPS
            )
        )
    }
}