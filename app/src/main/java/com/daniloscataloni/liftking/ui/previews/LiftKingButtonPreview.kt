package com.daniloscataloni.liftking.ui.previews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniloscataloni.liftking.ui.components.LiftKingButton
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun LiftKingButtonPreview() {
    LiftKingTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LiftKingButton(
                text = "Botão Light",
                isLight = true,
                onClick = {}
            )
            LiftKingButton(
                modifier = Modifier.padding(top = 8.dp),
                text = "Botão Dark",
                isLight = false,
                onClick = {}
            )
        }
    }
}
