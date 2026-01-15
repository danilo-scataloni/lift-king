package com.daniloscataloni.liftking.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daniloscataloni.liftking.ui.screens.ExerciseListScreen
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun ExerciseListPreview() {
    LiftKingTheme {
        ExerciseListScreen()
    }
}