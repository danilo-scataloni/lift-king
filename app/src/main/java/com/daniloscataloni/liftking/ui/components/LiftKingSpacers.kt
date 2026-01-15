package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmallSpacer() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun MediumSpacer() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun LargeSpacer() {
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun SmallHorizontalSpacer() {
    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
fun MediumHorizontalSpacer() {
    Spacer(modifier = Modifier.width(12.dp))
}
