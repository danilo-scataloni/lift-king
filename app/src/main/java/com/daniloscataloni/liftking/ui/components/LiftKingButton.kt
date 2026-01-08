package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.ui.utils.ButtonBackgroundGray
import com.daniloscataloni.liftking.ui.utils.Inter

@Composable
fun LiftKingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isLight: Boolean = false,
) {
    Button(
        modifier = modifier,
        colors = ButtonColors(
            containerColor = if (isLight) Color.White else ButtonBackgroundGray,
            contentColor = if (isLight) ButtonBackgroundGray else Color.White,
            disabledContainerColor = if (isLight) Color.White else ButtonBackgroundGray,
            disabledContentColor = if (isLight) ButtonBackgroundGray else Color.White
        ),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            color = if (isLight) BackgroundGray else Color.White,
            fontFamily = Inter
        )
    }
}