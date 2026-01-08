package com.daniloscataloni.liftking.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniloscataloni.liftking.ui.components.ExerciseCard
import com.daniloscataloni.liftking.ui.dialogs.ExerciseCreationDialog
import com.daniloscataloni.liftking.ui.utils.ButtonBackgroundGray
import com.daniloscataloni.liftking.ui.utils.DeepBlack
import com.daniloscataloni.liftking.ui.utils.Inter
import com.daniloscataloni.liftking.ui.viewmodels.ExerciseListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    viewModel: ExerciseListViewModel = koinViewModel()
) {

    val exercises = viewModel.getAllExercises().collectAsState(initial = emptyList())
    val showDialog = viewModel.showDialog.collectAsState()


    Scaffold(
        containerColor = DeepBlack,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Text(
                modifier = Modifier.padding(top = 48.dp, start = 16.dp, bottom = 16.dp),
                text = "Exercícios",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Inter,
                color = Color.White
            )
        }
    ) { paddingValues ->

        if (showDialog.value) {
            ExerciseCreationDialog(
                onDismiss = { viewModel.onDismissDialog() },
                onConfirm = { viewModel.onDismissDialog() }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(exercises.value) { exercise ->
                ExerciseCard(exercise = exercise)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(12.dp),
                onClick = { viewModel.onShowDialog() },
                containerColor = ButtonBackgroundGray
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar exercício",
                    tint = Color.White
                )
            }
        }
    }
}