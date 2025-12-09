package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daniloscataloni.liftking.entities.MuscleGroup
import com.daniloscataloni.liftking.entities.toReadableString
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.ui.utils.SmoothBorderGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupSelector(
    selectedMuscleGroup: MuscleGroup?,
    onMuscleGroupSelected: (MuscleGroup) -> Unit
) {
    var isMuscleGroupMenuOpen by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isMuscleGroupMenuOpen,
        onExpandedChange = { isMuscleGroupMenuOpen = !isMuscleGroupMenuOpen }
    ) {
        OutlinedTextField(
            value = selectedMuscleGroup?.toReadableString() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Grupos Musculares") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isMuscleGroupMenuOpen
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = SmoothBorderGrey,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = SmoothBorderGrey
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isMuscleGroupMenuOpen,
            onDismissRequest = { isMuscleGroupMenuOpen = false },
            border = BorderStroke(
                width = 0.dp,
                color = BackgroundGray
            )
        ) {
            MuscleGroup.entries.forEach { muscleGroup ->
                DropdownMenuItem(
                    text = { Text(muscleGroup.toReadableString()) },
                    onClick = {
                        onMuscleGroupSelected(muscleGroup)
                        isMuscleGroupMenuOpen = false
                    },
                )
            }
        }
    }
}