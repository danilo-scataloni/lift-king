package com.daniloscataloni.liftking.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.ui.utils.Inter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiftKingSimpleDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        content = {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 4.dp,
                color = BackgroundGray
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(bottom = 20.dp),
                        fontSize = 24.sp,
                        fontFamily = Inter,
                        color = Color.White
                    )
                    Text(
                        text = message,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                        fontSize = 12.sp,
                        fontFamily = Inter,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Row(modifier = Modifier) {
                        LiftKingButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            isLight = false,
                            onClick = { onDismiss() },
                            text = cancelButtonText
                        )
                        LiftKingButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            isLight = true,
                            onClick = { onConfirm() },
                            text = confirmButtonText
                        )
                    }
                }
            }
        }
    )
}