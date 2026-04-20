package com.daniloscataloni.liftking.ui.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.domain.models.WeightUnit

@Composable
fun WeightUnit.toDisplayLabel(): String = stringResource(displayLabelRes())

@Composable
fun WeightUnit.toShortLabel(): String = stringResource(shortLabelRes())

@StringRes
private fun WeightUnit.displayLabelRes(): Int {
    return when (this) {
        WeightUnit.KG -> R.string.weight_unit_kg_display
        WeightUnit.LIBRAS -> R.string.weight_unit_lb_display
        WeightUnit.TIJOLINHOS -> R.string.weight_unit_blocks_display
    }
}

@StringRes
private fun WeightUnit.shortLabelRes(): Int {
    return when (this) {
        WeightUnit.KG -> R.string.weight_unit_kg_short
        WeightUnit.LIBRAS -> R.string.weight_unit_lb_short
        WeightUnit.TIJOLINHOS -> R.string.weight_unit_blocks_short
    }
}
