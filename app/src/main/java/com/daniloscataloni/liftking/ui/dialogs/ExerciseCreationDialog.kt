package com.daniloscataloni.liftking.ui.dialogs

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.daniloscataloni.liftking.ui.components.LiftKingButton
import com.daniloscataloni.liftking.ui.components.MuscleGroupSelector
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.ui.utils.Inter
import com.daniloscataloni.liftking.viewmodels.ExerciseCreationViewModel
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
                    fontFamily = Inter,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Nome do exercício",
                    fontSize = 14.sp,
                    fontFamily = Inter,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = uiState.exercise.description,
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
                    placeholder = { Text(text = "Digite o nome do exercício", fontFamily = Inter) },
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(
                        fontFamily = Inter,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Grupo muscular principal",
                    fontSize = 14.sp,
                    fontFamily = Inter,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                MuscleGroupSelector(
                    selectedMuscleGroup = uiState.exercise.primaryMuscleGroup,
                    onMuscleGroupSelected = { viewModel.onPrimaryMuscleSelected(it) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Grupo muscular auxiliar",
                    fontSize = 14.sp,
                    fontFamily = Inter,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                MuscleGroupSelector(
                    selectedMuscleGroup = uiState.exercise.secondaryMuscleGroups,
                    onMuscleGroupSelected = { viewModel.onSecondaryMuscleSelected(it) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    LiftKingButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        isLight = false,
                        onClick = { onDismiss() },
                        text = "Cancelar"
                    )
                    LiftKingButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        isLight = true,
                        onClick = {
                            viewModel.onSaveExercise()
                            onConfirm()
                        },
                        text = "Adicionar"
                    )
                }
            }
        }
    }
}
