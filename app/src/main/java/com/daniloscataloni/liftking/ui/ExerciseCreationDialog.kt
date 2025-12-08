package com.daniloscataloni.liftking.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniloscataloni.liftking.ui.components.MuscleGroupSelector
import com.daniloscataloni.liftking.ui.utils.ButtonBackgroundGray
import com.daniloscataloni.liftking.ui.utils.BorderGray
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.viewmodels.ExerciseCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCreationDialog(
    modifier: Modifier = Modifier,
    viewModel: ExerciseCreationViewModel = viewModel()
) {
    val primaryMuscleGroup by viewModel.primaryMuscleGroup.collectAsState()
    val secondaryMuscleGroup by viewModel.secondaryMuscleGroup.collectAsState()
    val exerciseName by viewModel.exerciseName.collectAsState()

    BasicAlertDialog(
        onDismissRequest = {},
        modifier = modifier,
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
            shadowElevation = 4.dp,
            color = BackgroundGray
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Adicionar exercício",
                    fontSize = 24.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Nome do exercício",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = exerciseName,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledContainerColor = BackgroundGray,
                        focusedContainerColor = BackgroundGray,
                        unfocusedContainerColor = BackgroundGray
                    ),
                    onValueChange = { viewModel.onExerciseNameChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    singleLine = true,
                    enabled = true,
                    placeholder = { Text("Digite o nome do exercício") },
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Grupo muscular principal",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                MuscleGroupSelector(
                    selectedMuscleGroup = primaryMuscleGroup,
                    onMuscleGroupSelected = { viewModel.onMuscleGroup1Selected(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Grupo muscular auxiliar",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                MuscleGroupSelector(
                    selectedMuscleGroup = secondaryMuscleGroup,
                    onMuscleGroupSelected = { viewModel.onMuscleGroup2Selected(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        colors = ButtonColors(
                            containerColor = ButtonBackgroundGray,
                            contentColor = Color.White,
                            disabledContainerColor = ButtonBackgroundGray,
                            disabledContentColor = Color.White
                        ),
                        onClick = {}
                    ) {
                        Text(
                            text = "Cancelar",
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        colors = ButtonColors(
                            containerColor = Color.White,
                            contentColor = ButtonBackgroundGray,
                            disabledContainerColor = Color.White,
                            disabledContentColor = ButtonBackgroundGray,

                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = BorderGray
                        ),
                        onClick = {viewModel.onSaveExercise()}
                    ) {
                        Text(
                            text = "Adicionar"
                        )
                    }
                }
            }
        }
    }
}
