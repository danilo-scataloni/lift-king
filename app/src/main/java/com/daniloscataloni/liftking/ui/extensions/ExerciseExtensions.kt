package com.daniloscataloni.liftking.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.domain.models.Exercise

@Composable
fun Exercise.toDisplayName(): String {
    return description.ifBlank { stringResource(R.string.error_unknown_exercise) }
}
