package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniloscataloni.liftking.R

@Composable
fun DialogButtonRow(
    modifier: Modifier = Modifier,
    cancelText: String? = null,
    confirmText: String? = null,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    confirmEnabled: Boolean = true
) {
    val actualCancelText = cancelText ?: stringResource(R.string.button_cancel)
    val actualConfirmText = confirmText ?: stringResource(R.string.button_confirm)

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
            text = actualCancelText
        )
        LiftKingButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            isLight = true,
            onClick = onConfirm,
            text = actualConfirmText,
            enabled = confirmEnabled
        )
    }
}
