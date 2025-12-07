package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.daniloscataloni.liftking.entities.MuscleGroup
import com.daniloscataloni.liftking.entities.toReadableString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupSelector(
    selectedMuscleGroup: MuscleGroup?,
    onMuscleGroupSelected: (MuscleGroup) -> Unit,
    modifier: Modifier
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
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isMuscleGroupMenuOpen,
            onDismissRequest = { isMuscleGroupMenuOpen = false }
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