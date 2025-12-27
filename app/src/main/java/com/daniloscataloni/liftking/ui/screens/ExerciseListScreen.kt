package com.daniloscataloni.liftking.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.daniloscataloni.liftking.ui.components.ExerciseCard
import com.daniloscataloni.liftking.ui.utils.Inter
import com.daniloscataloni.liftking.viewmodels.ExerciseListViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    viewModel: ExerciseListViewModel = koinViewModel()
) {

    val exercises = viewModel.getAllExercises().collectAsState(initial = emptyList())


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                text = "Exercícios salvos",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Inter
            )
        }
    ){
        LazyColumn {
            items(exercises.value){
                exercise ->
                ExerciseCard(
                    exercise = exercise
                )
            }
        }
    }
}