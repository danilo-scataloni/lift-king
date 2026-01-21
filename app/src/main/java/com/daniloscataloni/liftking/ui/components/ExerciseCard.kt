package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.entities.Exercise
import com.daniloscataloni.liftking.entities.toReadableString
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.ui.utils.BorderGray

@Composable
fun ExerciseCard(
    exercise: Exercise
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .height(80.dp),
        shape = RoundedCornerShape(10.dp),
        color = BackgroundGray
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .border(
                        width = 1.dp,
                        color = BorderGray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    modifier = Modifier.padding(8.dp).size(30.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(R.string.content_desc_exercise_icon)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                LiftKingTitle(text = exercise.description)

                Row {
                    LiftKingCaption(text = exercise.primaryMuscleGroup.toReadableString())

                    MediumHorizontalSpacer()

                    if (exercise.secondaryMuscleGroups != null) {
                        LiftKingCaption(text = exercise.secondaryMuscleGroups.toReadableString())
                    }
                }
            }

            Icon(
                modifier = Modifier.height(20.dp).padding(end = 20.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.content_desc_view_details)
            )
        }
    }
}