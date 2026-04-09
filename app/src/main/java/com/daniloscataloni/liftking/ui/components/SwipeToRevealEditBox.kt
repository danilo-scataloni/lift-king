package com.daniloscataloni.liftking.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Wraps [content] in a swipe-right-to-reveal edit button.
 *
 * Dragging right translates the card to reveal a white pencil button on the left.
 * The button surface is intentionally [revealWidth] + [cornerRadius] wide so it
 * fills the triangular gap created by the card's own rounded left corners — making
 * the card's border arc act as a clean separator between button and card.
 *
 * @param cornerRadius must match the corner radius of the card inside [content].
 */
@Composable
fun SwipeToRevealEditBox(
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    revealWidth: Dp = 64.dp,
    cornerRadius: Dp = 12.dp,
    isRevealed: Boolean = false,
    onRevealStateChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val revealWidthPx = with(density) { revealWidth.toPx() }
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isRevealed) {
        val target = if (isRevealed) revealWidthPx else 0f
        offsetX.animateTo(target, animationSpec = spring(stiffness = Spring.StiffnessMedium))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            // IntrinsicSize.Min lets fillMaxHeight() work correctly inside a LazyColumn
            .height(IntrinsicSize.Min)
            // Clip the translated card so it doesn't overflow on the right side
            .clipToBounds(),
    ) {
        // Layer 1 — white edit panel, behind the card.
        // Width = revealWidth + cornerRadius so the white fills the triangular gap
        // created by the card's rounded left corners when the card is translated right.
        Surface(
            modifier = Modifier
                .width(revealWidth + cornerRadius)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .graphicsLayer {
                    alpha = (offsetX.value / revealWidthPx).coerceIn(0f, 1f)
                },
            shape = RoundedCornerShape(
                topStart = cornerRadius,
                bottomStart = cornerRadius,
                topEnd = 0.dp,
                bottomEnd = 0.dp,
            ),
            color = Color.White,
        ) {
            // Keep the icon centered within the visible revealWidth portion only
            Box(
                modifier = Modifier
                    .width(revealWidth)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                            onRevealStateChange(false)
                        }
                        onEditClick()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color.Black,
                    )
                }
            }
        }

        // Layer 2 — card content, slides right on drag.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { translationX = offsetX.value }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (offsetX.value > revealWidthPx * 0.4f) {
                                    offsetX.animateTo(
                                        revealWidthPx,
                                        spring(stiffness = Spring.StiffnessMedium),
                                    )
                                    onRevealStateChange(true)
                                } else {
                                    offsetX.animateTo(
                                        0f,
                                        spring(stiffness = Spring.StiffnessMedium),
                                    )
                                    onRevealStateChange(false)
                                }
                            }
                        },
                        onDragCancel = {
                            scope.launch {
                                offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                                onRevealStateChange(false)
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                // Only allow dragging to the right
                                val newValue = (offsetX.value + dragAmount).coerceIn(0f, revealWidthPx)
                                offsetX.snapTo(newValue)
                            }
                        },
                    )
                },
        ) {
            content()
        }
    }
}
