package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DialogButtonRow(
    cancelText: String = "Cancelar",
    confirmText: String = "Confirmar",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LiftKingButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            isLight = false,
            onClick = onCancel,
            text = cancelText
        )
        LiftKingButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            isLight = true,
            onClick = onConfirm,
            text = confirmText
        )
    }
}
