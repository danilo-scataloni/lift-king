package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.domain.models.WeightUnit
import com.daniloscataloni.liftking.ui.extensions.toReadableString
import com.daniloscataloni.liftking.ui.theme.BackgroundGray
import com.daniloscataloni.liftking.ui.theme.BorderGray
import com.daniloscataloni.liftking.ui.theme.SmoothGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExerciseDialog(
    exerciseName: String,
    primaryMuscle: MuscleGroup,
    secondaryMuscle: MuscleGroup?,
    weightUnit: WeightUnit,
    onNameChange: (String) -> Unit,
    onPrimaryMuscleChange: (MuscleGroup) -> Unit,
    onSecondaryMuscleChange: (MuscleGroup?) -> Unit,
    onWeightUnitChange: (WeightUnit) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                LiftKingHeading(text = stringResource(R.string.dialog_exercise_create_title))
                MediumSpacer()

                ExerciseFormContent(
                    exerciseName = exerciseName,
                    primaryMuscle = primaryMuscle,
                    secondaryMuscle = secondaryMuscle,
                    weightUnit = weightUnit,
                    onNameChange = onNameChange,
                    onPrimaryMuscleChange = onPrimaryMuscleChange,
                    onSecondaryMuscleChange = onSecondaryMuscleChange,
                    onWeightUnitChange = onWeightUnitChange
                )

                MediumSpacer()

                DialogButtonRow(
                    onCancel = onDismiss,
                    onConfirm = onConfirm,
                    confirmEnabled = exerciseName.isNotBlank()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExerciseDialog(
    exerciseName: String,
    primaryMuscle: MuscleGroup,
    secondaryMuscle: MuscleGroup?,
    weightUnit: WeightUnit,
    onNameChange: (String) -> Unit,
    onPrimaryMuscleChange: (MuscleGroup) -> Unit,
    onSecondaryMuscleChange: (MuscleGroup?) -> Unit,
    onWeightUnitChange: (WeightUnit) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                LiftKingHeading(text = stringResource(R.string.dialog_exercise_edit_title))
                MediumSpacer()

                ExerciseFormContent(
                    exerciseName = exerciseName,
                    primaryMuscle = primaryMuscle,
                    secondaryMuscle = secondaryMuscle,
                    weightUnit = weightUnit,
                    onNameChange = onNameChange,
                    onPrimaryMuscleChange = onPrimaryMuscleChange,
                    onSecondaryMuscleChange = onSecondaryMuscleChange,
                    onWeightUnitChange = onWeightUnitChange
                )

                MediumSpacer()

                DialogButtonRow(
                    onCancel = onDismiss,
                    onConfirm = onConfirm,
                    confirmEnabled = exerciseName.isNotBlank()
                )
            }
        }
    }
}

@Composable
private fun ExerciseFormContent(
    exerciseName: String,
    primaryMuscle: MuscleGroup,
    secondaryMuscle: MuscleGroup?,
    weightUnit: WeightUnit,
    onNameChange: (String) -> Unit,
    onPrimaryMuscleChange: (MuscleGroup) -> Unit,
    onSecondaryMuscleChange: (MuscleGroup?) -> Unit,
    onWeightUnitChange: (WeightUnit) -> Unit
) {
    OutlinedTextField(
        value = exerciseName,
        onValueChange = onNameChange,
        label = { Text(stringResource(R.string.label_exercise_name)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = BorderGray
        )
    )

    MediumSpacer()

    Text(
        text = stringResource(R.string.label_muscle_group_primary),
        style = MaterialTheme.typography.labelMedium,
        color = SmoothGray
    )
    SmallSpacer()
    MuscleGroupSelector(
        selectedMuscle = primaryMuscle,
        onSelect = { muscle -> muscle?.let(onPrimaryMuscleChange) },
        excludeMuscle = secondaryMuscle
    )

    MediumSpacer()

    Text(
        text = stringResource(R.string.label_muscle_group_secondary),
        style = MaterialTheme.typography.labelMedium,
        color = SmoothGray
    )
    SmallSpacer()
    MuscleGroupSelector(
        selectedMuscle = secondaryMuscle,
        onSelect = { muscle ->
            if (muscle == secondaryMuscle) {
                onSecondaryMuscleChange(null)
            } else {
                onSecondaryMuscleChange(muscle)
            }
        },
        excludeMuscle = primaryMuscle,
        allowNull = true
    )

    MediumSpacer()

    Text(
        text = stringResource(R.string.label_weight_unit),
        style = MaterialTheme.typography.labelMedium,
        color = SmoothGray
    )
    SmallSpacer()
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        WeightUnit.entries.forEachIndexed { index, unit ->
            SegmentedButton(
                selected = unit == weightUnit,
                onClick = { onWeightUnitChange(unit) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = WeightUnit.entries.size
                ),
                icon = {},
                label = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (unit == weightUnit) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = unit.displayLabel,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun MuscleGroupSelector(
    selectedMuscle: MuscleGroup?,
    onSelect: (MuscleGroup?) -> Unit,
    excludeMuscle: MuscleGroup? = null,
    allowNull: Boolean = false
) {
    val muscleGroups = MuscleGroup.entries.filter { it != excludeMuscle }

    LazyColumn(
        modifier = Modifier.height(120.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (allowNull) {
            item {
                val isSelected = selectedMuscle == null
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(null) },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            BackgroundGray
                        }
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else BorderGray
                    )
                ) {
                    Text(
                        text = stringResource(R.string.label_none),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        items(muscleGroups) { muscle ->
            val isSelected = muscle == selectedMuscle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(muscle) },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        BackgroundGray
                    }
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) MaterialTheme.colorScheme.primary else BorderGray
                )
            ) {
                Text(
                    text = muscle.toReadableString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteExerciseFromLibraryConfirmDialog(
    exerciseName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LiftKingHeading(text = stringResource(R.string.dialog_exercise_delete_title))

                MediumSpacer()

                Text(
                    text = stringResource(R.string.dialog_exercise_delete_message, exerciseName),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SmoothGray,
                    textAlign = TextAlign.Center
                )

                MediumSpacer()

                DialogButtonRow(
                    cancelText = stringResource(R.string.button_cancel),
                    confirmText = stringResource(R.string.button_delete),
                    onCancel = onDismiss,
                    onConfirm = onConfirm
                )
            }
        }
    }
}
