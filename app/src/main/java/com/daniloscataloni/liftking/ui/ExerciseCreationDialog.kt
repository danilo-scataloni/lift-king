package com.daniloscataloni.liftking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniloscataloni.liftking.ui.components.MuscleGroupSelector
import com.daniloscataloni.liftking.viewmodels.ExerciseCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCreationDialog(
    modifier: Modifier = Modifier,
    viewModel: ExerciseCreationViewModel = viewModel()
) {
    val selectedMuscleGroups by viewModel.selectedMuscleGroup.collectAsState()

    BasicAlertDialog(
        onDismissRequest = {},
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Exercise Creation Dialog")

                MuscleGroupSelector(
                    selectedMuscleGroup = selectedMuscleGroups,
                    onMuscleGroupSelected = { viewModel.onMuscleGroupClick(it) },
                    modifier = Modifier)
            }
        }
    }
}
