package com.daniloscataloni.liftking.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.domain.models.MuscleGroup

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
