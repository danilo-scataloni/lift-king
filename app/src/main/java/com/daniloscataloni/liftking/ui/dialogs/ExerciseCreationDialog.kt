package com.daniloscataloni.liftking.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.ui.components.DialogButtonRow
import com.daniloscataloni.liftking.ui.components.LargeSpacer
import com.daniloscataloni.liftking.ui.components.LiftKingHeading
import com.daniloscataloni.liftking.ui.components.LiftKingLabel
import com.daniloscataloni.liftking.ui.components.LiftKingTextField
import com.daniloscataloni.liftking.ui.components.MuscleGroupSelector
import com.daniloscataloni.liftking.ui.components.SmallSpacer
import com.daniloscataloni.liftking.ui.viewmodels.ExerciseCreationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCreationDialog(
    viewModel: ExerciseCreationViewModel = koinViewModel(),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    BasicAlertDialog(
        onDismissRequest = {},
        modifier = Modifier.fillMaxWidth(),
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SmallSpacer()

                LiftKingHeading(text = stringResource(R.string.dialog_exercise_add_title))

                LargeSpacer()

                LiftKingLabel(text = stringResource(R.string.label_exercise_name))

                SmallSpacer()

                LiftKingTextField(
                    value = uiState.exercise.description,
                    onValueChange = { viewModel.onExerciseNameChange(it) },
                    placeholder = stringResource(R.string.placeholder_exercise_name)
                )

                LargeSpacer()

                LiftKingLabel(text = stringResource(R.string.label_muscle_group_main))

                SmallSpacer()

                MuscleGroupSelector(
                    selectedMuscleGroup = uiState.exercise.primaryMuscleGroup,
                    onMuscleGroupSelected = { viewModel.onPrimaryMuscleSelected(it) }
                )

                LargeSpacer()

                LiftKingLabel(text = stringResource(R.string.label_muscle_group_auxiliary))

                SmallSpacer()

                MuscleGroupSelector(
                    selectedMuscleGroup = uiState.exercise.secondaryMuscleGroups,
                    onMuscleGroupSelected = { viewModel.onSecondaryMuscleSelected(it) }
                )

                LargeSpacer()

                DialogButtonRow(
                    modifier = Modifier.fillMaxHeight(),
                    cancelText = stringResource(R.string.button_cancel),
                    confirmText = stringResource(R.string.button_add),
                    onCancel = onDismiss,
                    onConfirm = {
                        viewModel.onSaveExercise()
                        onConfirm()
                    }
                )
            }
        }
    }
}
