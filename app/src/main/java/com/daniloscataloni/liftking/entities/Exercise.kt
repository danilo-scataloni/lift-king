package com.daniloscataloni.liftking.entities

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.daniloscataloni.liftking.R

data class Exercise (
    val id: Int,
    val description: String,
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: MuscleGroup?
)

enum class MuscleGroup() {
    CHEST,
    BACK,
    QUADS,
    HAMSTRINGS,
    BICEPS,
    TRICEPS,
    SHOULDERS,
    CALVES,
    ABS,
    FOREARMS
}

@Composable
fun MuscleGroup.toReadableString(): String {
    return when (this) {
        MuscleGroup.CHEST -> stringResource(R.string.muscle_group_chest)
        MuscleGroup.BACK -> stringResource(R.string.muscle_group_back)
        MuscleGroup.QUADS -> stringResource(R.string.muscle_group_quads)
        MuscleGroup.HAMSTRINGS -> stringResource(R.string.muscle_group_hamstrings)
        MuscleGroup.BICEPS -> stringResource(R.string.muscle_group_biceps)
        MuscleGroup.TRICEPS -> stringResource(R.string.muscle_group_triceps)
        MuscleGroup.SHOULDERS -> stringResource(R.string.muscle_group_shoulders)
        MuscleGroup.CALVES -> stringResource(R.string.muscle_group_calves)
        MuscleGroup.ABS -> stringResource(R.string.muscle_group_abs)
        MuscleGroup.FOREARMS -> stringResource(R.string.muscle_group_forearms)
    }
}
