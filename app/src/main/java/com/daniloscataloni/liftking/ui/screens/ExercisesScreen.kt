package com.daniloscataloni.liftking.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.ui.components.CreateExerciseDialog
import com.daniloscataloni.liftking.ui.components.DeleteExerciseFromLibraryConfirmDialog
import com.daniloscataloni.liftking.ui.components.EditExerciseDialog
import com.daniloscataloni.liftking.ui.components.MediumSpacer
import com.daniloscataloni.liftking.ui.extensions.toReadableString
import com.daniloscataloni.liftking.ui.theme.BackgroundGray
import com.daniloscataloni.liftking.ui.theme.BorderGray
import com.daniloscataloni.liftking.ui.viewmodels.ExercisesUiState
import com.daniloscataloni.liftking.ui.viewmodels.ExercisesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExercisesScreen(
    viewModel: ExercisesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var exerciseToDelete by remember { mutableStateOf<Exercise?>(null) }

    ExerciseLibraryDialogs(
        uiState = uiState,
        viewModel = viewModel,
        exerciseToDelete = exerciseToDelete,
        onExerciseToDeleteChange = { exerciseToDelete = it }
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ExercisesTopBar(
                uiState = uiState,
                onDeleteErrorDismiss = viewModel.clearDeleteExerciseError,
                onFilterSelected = viewModel::onMuscleFilterSelected
            )
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
        ExercisesContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onClearFilter = { viewModel.onMuscleFilterSelected(null) },
            onEditExercise = viewModel::setExerciseForEditing,
            onDeleteExercise = { exerciseToDelete = it }
        )
    }
}

@Composable
private fun ExerciseLibraryDialogs(
    uiState: ExercisesUiState,
    viewModel: ExercisesViewModel,
    exerciseToDelete: Exercise?,
    onExerciseToDeleteChange: (Exercise?) -> Unit
) {
    CreateExerciseFormDialog(uiState = uiState, viewModel = viewModel)
    EditExerciseFormDialog(uiState = uiState, viewModel = viewModel)
    DeleteExerciseDialog(
        viewModel = viewModel,
        exerciseToDelete = exerciseToDelete,
        onExerciseToDeleteChange = onExerciseToDeleteChange
    )
}

@Composable
private fun CreateExerciseFormDialog(
    uiState: ExercisesUiState,
    viewModel: ExercisesViewModel
) {
    if (!uiState.showCreateDialog) return

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

@Composable
private fun EditExerciseFormDialog(
    uiState: ExercisesUiState,
    viewModel: ExercisesViewModel
) {
    if (!uiState.showEditDialog) return

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

@Composable
private fun DeleteExerciseDialog(
    viewModel: ExercisesViewModel,
    exerciseToDelete: Exercise?,
    onExerciseToDeleteChange: (Exercise?) -> Unit
) {
    exerciseToDelete ?: return

    DeleteExerciseFromLibraryConfirmDialog(
        exerciseName = exerciseToDelete.description,
        onDismiss = { onExerciseToDeleteChange(null) },
        onConfirm = {
            viewModel.deleteExercise(exerciseToDelete)
            onExerciseToDeleteChange(null)
        }
    )
}

@Composable
private fun ExercisesTopBar(
    uiState: ExercisesUiState,
    onDeleteErrorDismiss: () -> Unit,
    onFilterSelected: (MuscleGroup?) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.screen_exercises_title),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        ActiveFilterSummary(uiState = uiState)
        DeleteExerciseError(
            isVisible = uiState.deleteExerciseError,
            onDismiss = onDeleteErrorDismiss
        )
        if (uiState.allExercisesCount > 0) {
            ExerciseMuscleFilters(
                selectedMuscleFilter = uiState.selectedMuscleFilter,
                onFilterSelected = onFilterSelected
            )
        }
    }
}

@Composable
private fun ActiveFilterSummary(
    uiState: ExercisesUiState
) {
    val activeFilter = uiState.selectedMuscleFilter ?: return
    if (uiState.allExercisesCount == 0) return

    Text(
        text = pluralStringResource(
            R.plurals.screen_exercises_filtered_count,
            uiState.exercises.size,
            activeFilter.toReadableString(),
            uiState.exercises.size
        ),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
    )
}

@Composable
private fun DeleteExerciseError(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    Text(
        text = stringResource(R.string.dialog_exercise_delete_error),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.clickable(onClick = onDismiss)
    )
}

@Composable
private fun ExercisesContent(
    uiState: ExercisesUiState,
    paddingValues: PaddingValues,
    onClearFilter: () -> Unit,
    onEditExercise: (Exercise) -> Unit,
    onDeleteExercise: (Exercise) -> Unit
) {
    when {
        uiState.allExercisesCount == 0 -> EmptyExercisesLibraryState(paddingValues = paddingValues)
        uiState.exercises.isEmpty() && uiState.selectedMuscleFilter != null -> EmptyFilteredExercisesState(
            paddingValues = paddingValues,
            activeFilter = uiState.selectedMuscleFilter,
            onClearFilter = onClearFilter
        )
        else -> ExerciseLibraryList(
            exercises = uiState.exercises,
            paddingValues = paddingValues,
            onEditExercise = onEditExercise,
            onDeleteExercise = onDeleteExercise
        )
    }
}

@Composable
private fun EmptyExercisesLibraryState(
    paddingValues: PaddingValues
) {
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
}

@Composable
private fun EmptyFilteredExercisesState(
    paddingValues: PaddingValues,
    activeFilter: MuscleGroup?,
    onClearFilter: () -> Unit
) {
    val muscleFilter = activeFilter ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(
                R.string.screen_exercises_empty_filtered_title,
                muscleFilter.toReadableString()
            ),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        MediumSpacer()
        Text(
            text = stringResource(R.string.screen_exercises_empty_filtered_subtitle),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
            fontSize = 14.sp
        )
        TextButton(onClick = onClearFilter) {
            Text(text = stringResource(R.string.button_clear_filter))
        }
    }
}

@Composable
private fun ExerciseLibraryList(
    exercises: List<Exercise>,
    paddingValues: PaddingValues,
    onEditExercise: (Exercise) -> Unit,
    onDeleteExercise: (Exercise) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(exercises, key = { it.id }) { exercise ->
            ExerciseLibraryCard(
                exercise = exercise,
                onEditClick = { onEditExercise(exercise) },
                onDeleteClick = { onDeleteExercise(exercise) }
            )
        }
    }
}

@Composable
private fun ExerciseMuscleFilters(
    selectedMuscleFilter: MuscleGroup?,
    onFilterSelected: (MuscleGroup?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedMuscleFilter == null,
                onClick = { onFilterSelected(null) },
                label = { Text(text = stringResource(R.string.screen_exercises_filter_all)) }
            )
        }

        items(
            items = MuscleGroup.entries.toList(),
            key = { muscle -> muscle.name }
        ) { muscle ->
            FilterChip(
                selected = muscle == selectedMuscleFilter,
                onClick = { onFilterSelected(muscle) },
                label = { Text(text = muscle.toReadableString()) }
            )
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
            ExerciseLibraryCardContent(exercise = exercise)
            ExerciseLibraryCardActions(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun RowScope.ExerciseLibraryCardContent(
    exercise: Exercise
) {
    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = exercise.description,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        MediumSpacer()
        Text(
            text = buildMuscleLabel(exercise = exercise),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
        )
        Text(
            text = exercise.weightUnit.shortLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
        )
    }
}

@Composable
private fun ExerciseLibraryCardActions(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
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

@Composable
private fun buildMuscleLabel(
    exercise: Exercise
): String {
    return buildString {
        append(exercise.primaryMuscleGroup.toReadableString())
        exercise.secondaryMuscleGroups?.let { secondary ->
            append(" - ")
            append(secondary.toReadableString())
        }
    }
}
