package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniloscataloni.liftking.entities.Exercise
import com.daniloscataloni.liftking.entities.toReadableString
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.ui.utils.BorderGray
import com.daniloscataloni.liftking.ui.utils.Inter
import com.daniloscataloni.liftking.ui.utils.SmoothGray

@OptIn(ExperimentalMaterial3Api::class)
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
        tonalElevation = 8.dp,
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
            ){
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    modifier = Modifier.padding(8.dp).size(30.dp),
                    tint = Color.White,
                    contentDescription = null
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.description,
                    color = Color.White,
                    fontFamily = Inter,
                    fontSize = 20.sp
                )
                Row {
                    Text(
                        text = exercise.primaryMuscleGroup.toReadableString(),
                        color = SmoothGray,
                        fontFamily = Inter,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    if (exercise.secondaryMuscleGroups != null){
                        Text(
                            text = exercise.secondaryMuscleGroups.toReadableString(),
                            color = SmoothGray,
                            fontFamily = Inter,
                            fontSize = 12.sp,
                        )
                    }
                }

            }

            Icon(
                modifier = Modifier.height(20.dp).padding(end = 20.dp),
                imageVector = Icons.Default.ArrowForward,
                tint = SmoothGray,
                contentDescription = null
            )
        }
    }
}