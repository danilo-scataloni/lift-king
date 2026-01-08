package com.daniloscataloni.liftking.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daniloscataloni.liftking.ui.dialogs.ExerciseCreationDialog


@Preview(showSystemUi = true)
@Composable
fun ExerciseCreationDialogPreview() {
    ExerciseCreationDialog(
        onConfirm = {},
        onDismiss = {}
    )
}