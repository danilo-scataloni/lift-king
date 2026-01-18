package com.daniloscataloni.liftking.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniloscataloni.liftking.entities.Exercise
import com.daniloscataloni.liftking.entities.MuscleGroup
import com.daniloscataloni.liftking.entities.SetLog
import com.daniloscataloni.liftking.entities.toReadableString
import com.daniloscataloni.liftking.ui.components.DialogButtonRow
import com.daniloscataloni.liftking.ui.components.LiftKingHeading
import com.daniloscataloni.liftking.ui.components.MediumSpacer
import com.daniloscataloni.liftking.ui.components.SmallSpacer
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.ui.utils.BorderGray
import com.daniloscataloni.liftking.ui.utils.SmoothGray
import com.daniloscataloni.liftking.ui.viewmodels.ExerciseWithSets
import com.daniloscataloni.liftking.ui.viewmodels.TrainingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrainingScreen(
    workoutId: Long,
    viewModel: TrainingViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onComplete: () -> Unit
) {
    LaunchedEffect(workoutId) {
        viewModel.loadWorkout(workoutId)
    }

    val uiState by viewModel.uiState.collectAsState()
    var showAddSetDialog by remember { mutableStateOf<ExerciseWithSets?>(null) }
    var showFinishConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 8.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = uiState.workoutName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = { viewModel.showAddExerciseDialog() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar exercício",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                FloatingActionButton(
                    onClick = { showFinishConfirmDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Finalizar treino",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        // Dialog de confirmação para finalizar treino
        if (showFinishConfirmDialog) {
            FinishWorkoutDialog(
                workoutName = uiState.workoutName,
                setsCount = uiState.exercises.sumOf { it.currentSets.size },
                onDismiss = { showFinishConfirmDialog = false },
                onConfirm = {
                    showFinishConfirmDialog = false
                    viewModel.completeSession()
                    onComplete()
                }
            )
        }

        showAddSetDialog?.let { exerciseWithSets ->
            AddSetDialog(
                exerciseName = exerciseWithSets.exercise.description,
                onDismiss = { showAddSetDialog = null },
                onConfirm = { weight, reps, rir ->
                    viewModel.logSet(
                        exerciseId = exerciseWithSets.exercise.id,
                        weight = weight,
                        reps = reps,
                        rir = rir
                    )
                    showAddSetDialog = null
                }
            )
        }
        if (uiState.showAddExerciseDialog) {
            AddExerciseDialog(
                exercises = uiState.availableExercises,
                onDismiss = { viewModel.hideAddExerciseDialog() },
                onSelect = { exercise -> viewModel.addExerciseToWorkout(exercise) },
                onCreateNew = { viewModel.showCreateExerciseDialog() }
            )
        }

        if (uiState.showCreateExerciseDialog) {
            CreateExerciseDialog(
                exerciseName = uiState.newExerciseName,
                primaryMuscle = uiState.newExercisePrimaryMuscle,
                secondaryMuscle = uiState.newExerciseSecondaryMuscle,
                onNameChange = { viewModel.onNewExerciseNameChange(it) },
                onPrimaryMuscleChange = { viewModel.onNewExercisePrimaryMuscleChange(it) },
                onSecondaryMuscleChange = { viewModel.onNewExerciseSecondaryMuscleChange(it) },
                onDismiss = { viewModel.hideCreateExerciseDialog() },
                onConfirm = { viewModel.createExerciseAndAddToWorkout() }
            )
        }

        uiState.editingSet?.let { set ->
            EditSetDialog(
                set = set,
                onDismiss = { viewModel.cancelEditingSet() },
                onConfirm = { weight, reps, rir -> viewModel.updateSet(weight, reps, rir) },
                onDelete = { viewModel.deleteSet() }
            )
        }

        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.exercises.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nenhum exercício neste treino",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                MediumSpacer()

                Text(
                    text = "Adicione exercícios ao treino primeiro",
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.exercises) { exerciseWithSets ->
                    ExerciseCard(
                        exerciseWithSets = exerciseWithSets,
                        onAddSet = { showAddSetDialog = exerciseWithSets },
                        onSetClick = { set -> viewModel.startEditingSet(set) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseCard(
    exerciseWithSets: ExerciseWithSets,
    onAddSet: () -> Unit,
    onSetClick: (SetLog) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundGray),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = exerciseWithSets.exercise.description,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = exerciseWithSets.exercise.primaryMuscleGroup.toReadableString(),
                style = MaterialTheme.typography.bodySmall,
                color = SmoothGray
            )

            MediumSpacer()

            Text(
                text = "Último treino",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = SmoothGray
            )
            SmallSpacer()

            SetTableHeader()

            if (exerciseWithSets.lastSets.isEmpty()) {
                Text(
                    text = "Sem registro anterior",
                    style = MaterialTheme.typography.bodySmall,
                    color = SmoothGray.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                exerciseWithSets.lastSets.forEach { set ->
                    SetRow(set = set, isCurrentSession = false)
                }
            }

            MediumSpacer()

            Text(
                text = "Treino atual",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            SmallSpacer()

            if (exerciseWithSets.currentSets.isEmpty()) {
                Text(
                    text = "Nenhuma série registrada",
                    style = MaterialTheme.typography.bodySmall,
                    color = SmoothGray.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                SetTableHeader()
                exerciseWithSets.currentSets.forEach { set ->
                    SetRow(
                        set = set,
                        isCurrentSession = true,
                        onClick = { onSetClick(set) }
                    )
                }
            }

            MediumSpacer()

            TextButton(
                onClick = onAddSet,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                SmallSpacer()

                Text(
                    text = "Adicionar série",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SetTableHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Série",
            style = MaterialTheme.typography.labelSmall,
            color = SmoothGray,
            modifier = Modifier.width(50.dp)
        )
        Text(
            text = "Peso",
            style = MaterialTheme.typography.labelSmall,
            color = SmoothGray,
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Reps",
            style = MaterialTheme.typography.labelSmall,
            color = SmoothGray,
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "RIR",
            style = MaterialTheme.typography.labelSmall,
            color = SmoothGray,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SetRow(
    set: SetLog,
    isCurrentSession: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val textColor = if (isCurrentSession) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${set.setNumber}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isCurrentSession) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.width(50.dp)
        )
        Text(
            text = "${set.weight}kg",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isCurrentSession) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "${set.reps}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isCurrentSession) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = set.rir?.toString() ?: "Falha",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isCurrentSession) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSetDialog(
    exerciseName: String,
    onDismiss: () -> Unit,
    onConfirm: (weight: Float, reps: Int, rir: Int?) -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var rir by remember { mutableStateOf("") }

    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LiftKingHeading(text = "Nova Série")
                Text(
                    text = exerciseName,
                    style = MaterialTheme.typography.bodySmall,
                    color = SmoothGray
                )

                MediumSpacer()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Peso (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BorderGray
                        )
                    )
                    OutlinedTextField(
                        value = reps,
                        onValueChange = { reps = it },
                        label = { Text("Reps") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BorderGray
                        )
                    )
                    OutlinedTextField(
                        value = rir,
                        onValueChange = { rir = it },
                        label = { Text("RIR") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BorderGray
                        )
                    )
                }

                MediumSpacer()

                DialogButtonRow(
                    onCancel = onDismiss,
                    onConfirm = {
                        val weightValue = weight.toFloatOrNull()
                        val repsValue = reps.toIntOrNull()
                        if (weightValue != null && repsValue != null) {
                            onConfirm(weightValue, repsValue, rir.toIntOrNull())
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditSetDialog(
    set: SetLog,
    onDismiss: () -> Unit,
    onConfirm: (weight: Float, reps: Int, rir: Int?) -> Unit,
    onDelete: () -> Unit
) {
    var weight by remember { mutableStateOf(set.weight.toString()) }
    var reps by remember { mutableStateOf(set.reps.toString()) }
    var rir by remember { mutableStateOf(set.rir?.toString() ?: "") }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        BasicAlertDialog(onDismissRequest = { showDeleteConfirm = false }) {
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
                    LiftKingHeading(text = "Excluir série?")
                    MediumSpacer()
                    Text(
                        text = "Essa ação não pode ser desfeita.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SmoothGray
                    )
                    MediumSpacer()
                    DialogButtonRow(
                        cancelText = "Cancelar",
                        confirmText = "Excluir",
                        onCancel = { showDeleteConfirm = false },
                        onConfirm = {
                            showDeleteConfirm = false
                            onDelete()
                        }
                    )
                }
            }
        }
    } else {
        BasicAlertDialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LiftKingHeading(text = "Editar Série ${set.setNumber}")

                    MediumSpacer()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = BorderGray
                            )
                        )
                        OutlinedTextField(
                            value = reps,
                            onValueChange = { reps = it },
                            label = { Text("Reps") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = BorderGray
                            )
                        )
                        OutlinedTextField(
                            value = rir,
                            onValueChange = { rir = it },
                            label = { Text("RIR") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = BorderGray
                            )
                        )
                    }

                    MediumSpacer()

                    // Botão de excluir
                    TextButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Excluir série",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    SmallSpacer()

                    DialogButtonRow(
                        onCancel = onDismiss,
                        onConfirm = {
                            val weightValue = weight.toFloatOrNull()
                            val repsValue = reps.toIntOrNull()
                            if (weightValue != null && repsValue != null) {
                                onConfirm(weightValue, repsValue, rir.toIntOrNull())
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinishWorkoutDialog(
    workoutName: String,
    setsCount: Int,
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
                // Ícone redondo com letra inicial
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = workoutName.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                MediumSpacer()

                LiftKingHeading(text = "Finalizar treino?")

                SmallSpacer()

                Text(
                    text = if (setsCount > 0) {
                        "Você registrou $setsCount ${if (setsCount == 1) "série" else "séries"} neste treino."
                    } else {
                        "Nenhuma série foi registrada neste treino."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = SmoothGray,
                    textAlign = TextAlign.Center
                )

                MediumSpacer()

                DialogButtonRow(
                    cancelText = "Continuar",
                    confirmText = "Finalizar",
                    onCancel = onDismiss,
                    onConfirm = onConfirm
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExerciseDialog(
    exercises: List<Exercise>,
    onDismiss: () -> Unit,
    onSelect: (Exercise) -> Unit,
    onCreateNew: () -> Unit
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
                LiftKingHeading(text = "Adicionar Exercício")
                MediumSpacer()

                // Botão para criar novo exercício
                TextButton(
                    onClick = onCreateNew,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Criar novo exercício",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                SmallSpacer()

                if (exercises.isEmpty()) {
                    Text(
                        text = "Nenhum exercício cadastrado ainda.",
                        color = SmoothGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    MediumSpacer()
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Fechar")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.height(300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(exercises) { exercise ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelect(exercise) },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = BackgroundGray),
                                border = BorderStroke(1.dp, BorderGray)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = exercise.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = exercise.primaryMuscleGroup.toReadableString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SmoothGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateExerciseDialog(
    exerciseName: String,
    primaryMuscle: MuscleGroup,
    secondaryMuscle: MuscleGroup?,
    onNameChange: (String) -> Unit,
    onPrimaryMuscleChange: (MuscleGroup) -> Unit,
    onSecondaryMuscleChange: (MuscleGroup?) -> Unit,
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
                LiftKingHeading(text = "Criar Exercício")
                MediumSpacer()

                OutlinedTextField(
                    value = exerciseName,
                    onValueChange = onNameChange,
                    label = { Text("Nome do exercício") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = BorderGray
                    )
                )

                MediumSpacer()

                // Seletor de grupo muscular primário
                Text(
                    text = "Grupo muscular primário",
                    style = MaterialTheme.typography.labelMedium,
                    color = SmoothGray
                )
                SmallSpacer()
                MuscleGroupSelector(
                    selectedMuscle = primaryMuscle,
                    onSelect = onPrimaryMuscleChange,
                    excludeMuscle = secondaryMuscle
                )

                MediumSpacer()

                // Seletor de grupo muscular secundário
                Text(
                    text = "Grupo muscular secundário (opcional)",
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
private fun MuscleGroupSelector(
    selectedMuscle: MuscleGroup?,
    onSelect: (MuscleGroup) -> Unit,
    excludeMuscle: MuscleGroup? = null,
    allowNull: Boolean = false
) {
    val muscleGroups = MuscleGroup.entries.filter { it != excludeMuscle }

    LazyColumn(
        modifier = Modifier.height(120.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(muscleGroups) { muscle ->
            val isSelected = muscle == selectedMuscle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(muscle) },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        BackgroundGray
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) MaterialTheme.colorScheme.primary else BorderGray
                )
            ) {
                Text(
                    text = muscle.toReadableString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
