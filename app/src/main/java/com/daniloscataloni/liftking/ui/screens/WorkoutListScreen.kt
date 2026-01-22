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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.domain.models.Workout
import com.daniloscataloni.liftking.ui.components.DialogButtonRow
import com.daniloscataloni.liftking.ui.components.LiftKingHeading
import com.daniloscataloni.liftking.ui.components.LiftKingTextField
import com.daniloscataloni.liftking.ui.components.MediumSpacer
import com.daniloscataloni.liftking.ui.theme.BackgroundGray
import com.daniloscataloni.liftking.ui.theme.BorderGray
import com.daniloscataloni.liftking.ui.viewmodels.WorkoutListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutListScreen(
    periodizationId: Long,
    viewModel: WorkoutListViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onWorkoutClick: (Workout) -> Unit
) {
    LaunchedEffect(periodizationId) {
        viewModel.setPeriodizationId(periodizationId)
    }

    val workouts by viewModel.workouts.collectAsState()
    val periodization by viewModel.periodizationName.collectAsState()
    val showDialog by viewModel.showCreateDialog.collectAsState()
    val newName by viewModel.newWorkoutName.collectAsState()

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
                        contentDescription = stringResource(R.string.content_desc_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Column {
                    Text(
                        text = stringResource(R.string.screen_workouts_title),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    periodization?.let {
                        Text(
                            text = it.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onShowCreateDialog() },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.content_desc_new_workout),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { paddingValues ->

        if (showDialog) {
            CreateWorkoutDialog(
                name = newName,
                onNameChange = { viewModel.onNameChange(it) },
                onDismiss = { viewModel.onDismissCreateDialog() },
                onConfirm = { viewModel.createWorkout() }
            )
        }

        if (workouts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.screen_workouts_empty_title),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                MediumSpacer()
                Text(
                    text = stringResource(R.string.screen_workouts_empty_subtitle),
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
                items(workouts) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onClick = { onWorkoutClick(workout) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkoutCard(
    workout: Workout,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundGray),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone redondo com letra inicial
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = workout.name.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = workout.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateWorkoutDialog(
    name: String,
    onNameChange: (String) -> Unit,
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
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LiftKingHeading(text = stringResource(R.string.dialog_workout_create_title))
                MediumSpacer()
                LiftKingTextField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = stringResource(R.string.dialog_workout_create_placeholder)
                )
                MediumSpacer()
                DialogButtonRow(
                    onCancel = onDismiss,
                    onConfirm = { if (name.isNotBlank()) onConfirm() }
                )
            }
        }
    }
}
