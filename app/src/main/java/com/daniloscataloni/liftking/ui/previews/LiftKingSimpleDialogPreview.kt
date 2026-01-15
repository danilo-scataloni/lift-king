package com.daniloscataloni.liftking.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daniloscataloni.liftking.ui.components.LiftKingSimpleDialog
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun LiftKingSimpleDialogPreview() {
    LiftKingTheme {
        LiftKingSimpleDialog(
            title = "Título da Dialog!",
            message = "Esta é uma mensagem de exemplo para a dialog do LiftKing.",
            confirmButtonText = "Confirmar",
            cancelButtonText = "Cancelar",
            onDismiss = { },
            onConfirm = { }
        )
    }
}