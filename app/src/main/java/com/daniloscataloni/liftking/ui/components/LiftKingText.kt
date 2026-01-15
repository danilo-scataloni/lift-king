package com.daniloscataloni.liftking.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LiftKingHeading(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun LiftKingTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun LiftKingBody(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun LiftKingLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun LiftKingCaption(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        textAlign = textAlign
    )
}
