package com.daniloscataloni.liftking.ui.previews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.ui.components.LiftKingLabel
import com.daniloscataloni.liftking.ui.components.MuscleGroupSelector
import com.daniloscataloni.liftking.ui.components.SmallSpacer
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun MuscleGroupSelectorEmptyPreview() {
    LiftKingTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LiftKingLabel(text = "Estado vazio")
            SmallSpacer()
            MuscleGroupSelector(
                selectedMuscleGroup = null,
                onMuscleGroupSelected = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun MuscleGroupSelectorSelectedPreview() {
    LiftKingTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LiftKingLabel(text = "Com seleção")
            SmallSpacer()
            MuscleGroupSelector(
                selectedMuscleGroup = MuscleGroup.CHEST,
                onMuscleGroupSelected = {}
            )
        }
    }
}
