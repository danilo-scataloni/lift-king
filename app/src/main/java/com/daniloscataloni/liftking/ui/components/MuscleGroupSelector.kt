package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.ui.extensions.toReadableString

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
            label = { Text(stringResource(R.string.label_muscle_groups)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isMuscleGroupMenuOpen
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isMuscleGroupMenuOpen,
            onDismissRequest = { isMuscleGroupMenuOpen = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            MuscleGroup.entries.forEach { muscleGroup ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = muscleGroup.toReadableString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onMuscleGroupSelected(muscleGroup)
                        isMuscleGroupMenuOpen = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}