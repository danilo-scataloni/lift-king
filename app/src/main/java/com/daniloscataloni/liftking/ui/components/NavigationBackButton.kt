package com.daniloscataloni.liftking.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.daniloscataloni.liftking.R

@Composable
fun NavigationBackButton(
    onBackClick: () -> Boolean
) {
    var isNavigatingBack by remember { mutableStateOf(false) }

    IconButton(
        enabled = !isNavigatingBack,
        onClick = {
            if (isNavigatingBack) return@IconButton
            isNavigatingBack = onBackClick()
        }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.content_desc_back),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
