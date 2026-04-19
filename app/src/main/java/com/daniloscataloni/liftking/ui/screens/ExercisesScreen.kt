package com.daniloscataloni.liftking.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.ui.components.CreateExerciseDialog
import com.daniloscataloni.liftking.ui.components.DeleteExerciseFromLibraryConfirmDialog
import com.daniloscataloni.liftking.ui.components.EditExerciseDialog
import com.daniloscataloni.liftking.ui.components.MediumSpacer
import com.daniloscataloni.liftking.ui.extensions.toReadableString
import com.daniloscataloni.liftking.ui.theme.BackgroundGray
import com.daniloscataloni.liftking.ui.theme.BorderGray
import com.daniloscataloni.liftking.ui.viewmodels.ExercisesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExercisesScreen(
    viewModel: ExercisesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var exerciseToDelete by remember { mutableStateOf<Exercise?>(null) }

    if (uiState.showCreateDialog) {
        CreateExerciseDialog(
            exerciseName = uiState.newExerciseName,
            primaryMuscle = uiState.newExercisePrimaryMuscle,
            secondaryMuscle = uiState.newExerciseSecondaryMuscle,
            weightUnit = uiState.newExerciseWeightUnit,
            onNameChange = viewModel::onNewExerciseNameChange,
            onPrimaryMuscleChange = viewModel::onNewExercisePrimaryMuscleChange,
            onSecondaryMuscleChange = viewModel::onNewExerciseSecondaryMuscleChange,
            onWeightUnitChange = viewModel::onNewExerciseWeightUnitChange,
            onDismiss = { viewModel.setCreateDialogVisible(false) },
            onConfirm = viewModel::createExercise
        )
    }

    if (uiState.showEditDialog) {
        EditExerciseDialog(
            exerciseName = uiState.editExerciseName,
            primaryMuscle = uiState.editExercisePrimaryMuscle,
            secondaryMuscle = uiState.editExerciseSecondaryMuscle,
            weightUnit = uiState.editExerciseWeightUnit,
            onNameChange = viewModel::onEditExerciseNameChange,
            onPrimaryMuscleChange = viewModel::onEditExercisePrimaryMuscleChange,
            onSecondaryMuscleChange = viewModel::onEditExerciseSecondaryMuscleChange,
            onWeightUnitChange = viewModel::onEditExerciseWeightUnitChange,
            onDismiss = { viewModel.setExerciseForEditing(null) },
            onConfirm = viewModel::confirmEditExercise
        )
    }

    exerciseToDelete?.let { exercise ->
        DeleteExerciseFromLibraryConfirmDialog(
            exerciseName = exercise.description,
            onDismiss = { exerciseToDelete = null },
            onConfirm = {
                viewModel.deleteExercise(exercise)
                exerciseToDelete = null
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.screen_exercises_title),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (uiState.deleteExerciseError) {
                    MediumSpacer()
                    Text(
                        text = stringResource(R.string.dialog_exercise_delete_error),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.clickable { viewModel.clearDeleteExerciseError() }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setCreateDialogVisible(true) },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.content_desc_new_exercise),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { paddingValues ->
        if (uiState.exercises.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.screen_exercises_empty_title),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                MediumSpacer()
                Text(
                    text = stringResource(R.string.screen_exercises_empty_subtitle),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.exercises, key = { it.id }) { exercise ->
                    ExerciseLibraryCard(
                        exercise = exercise,
                        onEditClick = { viewModel.setExerciseForEditing(exercise) },
                        onDeleteClick = { exerciseToDelete = exercise }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseLibraryCard(
    exercise: Exercise,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundGray),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.description,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                MediumSpacer()
                Text(
                    text = buildString {
                        append(exercise.primaryMuscleGroup.toReadableString())
                        exercise.secondaryMuscleGroups?.let { secondary ->
                            append(" - ")
                            append(secondary.toReadableString())
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                )
                Text(
                    text = exercise.weightUnit.shortLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
                )
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.content_desc_edit_exercise),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.content_desc_delete_exercise),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
