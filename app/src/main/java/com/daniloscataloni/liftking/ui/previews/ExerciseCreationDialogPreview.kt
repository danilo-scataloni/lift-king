package com.daniloscataloni.liftking.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daniloscataloni.liftking.ui.dialogs.ExerciseCreationDialog
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

@Preview(showSystemUi = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun ExerciseCreationDialogPreview() {
    LiftKingTheme {
        ExerciseCreationDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}