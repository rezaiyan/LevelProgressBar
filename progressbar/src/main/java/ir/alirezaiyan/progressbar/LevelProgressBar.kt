/*
 * Copyright 2016 alirezaiyann@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.alirezaiyan.progressbar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

private const val DEFAULT_START_ANGLE = 120f
private const val DEFAULT_TOTAL_ANGLE = 300f
private const val STEP_ARC_SWEEP = 10f
private const val STEP_ARC_GAP = 30f
private const val DISABLED_ALPHA = 0.4f
private const val ANIMATION_DURATION_MS = 1500

@Composable
fun LevelProgressBar(
    level: Int,
    modifier: Modifier = Modifier,
    maxLevel: Int = 10,
    progressColor: Color = Color.Green,
    unProgressColor: Color = Color.Gray,
    backgroundColor: Color = progressColor,
    textColor: Color = Color.White,
    strokeWidth: Dp = 10.dp,
    isStepProgress: Boolean = false,
    enabled: Boolean = true,
    imageBitmap: ImageBitmap? = null,
    animated: Boolean = true,
) {
    val coercedLevel = level.coerceIn(0, maxLevel)
    val targetProgress = coercedLevel.toFloat() / maxLevel.toFloat()

    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(targetProgress) {
        if (animated) {
            animatedProgress.animateTo(
                targetValue = targetProgress,
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_MS,
                    easing = FastOutSlowInEasing
                )
            )
        } else {
            animatedProgress.snapTo(targetProgress)
        }
    }

    val textMeasurer = rememberTextMeasurer()
    val displayLevel = if (animated) {
        (animatedProgress.value * maxLevel).toInt()
    } else {
        coercedLevel
    }

    val alpha = if (enabled) 1f else DISABLED_ALPHA

    Canvas(
        modifier = modifier
            .size(250.dp)
            .aspectRatio(1f)
    ) {
        val strokeWidthPx = strokeWidth.toPx()
        val canvasSize = min(size.width, size.height)
        val radius = canvasSize / 2.5f
        val center = Offset(size.width / 2f, size.height / 2f)

        val arcRect = Size(
            canvasSize - strokeWidthPx * 2,
            canvasSize - strokeWidthPx * 2
        )
        val arcTopLeft = Offset(
            (size.width - arcRect.width) / 2f,
            (size.height - arcRect.height) / 2f
        )

        // Draw background circle
        drawCircle(
            color = if (imageBitmap != null) backgroundColor else backgroundColor,
            radius = radius - strokeWidthPx,
            center = center,
            alpha = alpha
        )

        // Draw image bitmap if provided
        if (imageBitmap != null) {
            val imageSize = (radius - strokeWidthPx) * 2
            val scale = imageSize / min(imageBitmap.width, imageBitmap.height).toFloat()
            val scaledWidth = imageBitmap.width * scale
            val scaledHeight = imageBitmap.height * scale

            drawImage(
                image = imageBitmap,
                dstOffset = androidx.compose.ui.unit.IntOffset(
                    ((size.width - scaledWidth) / 2f).toInt(),
                    ((size.height - scaledHeight) / 2f).toInt()
                ),
                dstSize = androidx.compose.ui.unit.IntSize(
                    scaledWidth.toInt(),
                    scaledHeight.toInt()
                ),
                alpha = alpha
            )
        }

        val currentAngle = DEFAULT_TOTAL_ANGLE * animatedProgress.value

        if (isStepProgress) {
            drawStepProgress(
                arcTopLeft = arcTopLeft,
                arcSize = arcRect,
                currentAngle = currentAngle,
                strokeWidthPx = strokeWidthPx,
                progressColor = progressColor,
                unProgressColor = unProgressColor,
                alpha = alpha
            )
        } else {
            drawContinuousProgress(
                arcTopLeft = arcTopLeft,
                arcSize = arcRect,
                currentAngle = currentAngle,
                strokeWidthPx = strokeWidthPx,
                progressColor = progressColor,
                unProgressColor = unProgressColor,
                alpha = alpha
            )
        }

        // Draw level text if no image
        if (imageBitmap == null) {
            drawLevelText(
                textMeasurer = textMeasurer,
                level = displayLevel,
                center = center,
                radius = radius,
                textColor = textColor,
                alpha = alpha
            )
        }
    }
}

private fun DrawScope.drawContinuousProgress(
    arcTopLeft: Offset,
    arcSize: Size,
    currentAngle: Float,
    strokeWidthPx: Float,
    progressColor: Color,
    unProgressColor: Color,
    alpha: Float,
) {
    // Progress arc
    if (currentAngle > 0f) {
        drawArc(
            color = progressColor,
            startAngle = DEFAULT_START_ANGLE,
            sweepAngle = currentAngle,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt),
            alpha = alpha
        )
    }

    // Remaining arc
    val remainingAngle = DEFAULT_TOTAL_ANGLE - currentAngle
    if (remainingAngle > 0f) {
        drawArc(
            color = unProgressColor,
            startAngle = DEFAULT_START_ANGLE + currentAngle,
            sweepAngle = remainingAngle,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt),
            alpha = alpha
        )
    }
}

private fun DrawScope.drawStepProgress(
    arcTopLeft: Offset,
    arcSize: Size,
    currentAngle: Float,
    strokeWidthPx: Float,
    progressColor: Color,
    unProgressColor: Color,
    alpha: Float,
) {
    var step = STEP_ARC_SWEEP
    while (step <= DEFAULT_TOTAL_ANGLE) {
        val color = if (step <= currentAngle) progressColor else unProgressColor
        drawArc(
            color = color,
            startAngle = DEFAULT_START_ANGLE + step,
            sweepAngle = STEP_ARC_SWEEP,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
            alpha = alpha
        )
        step += STEP_ARC_GAP
    }
}

private fun DrawScope.drawLevelText(
    textMeasurer: TextMeasurer,
    level: Int,
    center: Offset,
    radius: Float,
    textColor: Color,
    alpha: Float,
) {
    val textStyle = TextStyle(
        color = textColor.copy(alpha = alpha),
        fontSize = (radius * 0.8f).toSp(),
    )
    val textLayoutResult = textMeasurer.measure(
        text = level.toString(),
        style = textStyle,
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = center.x - textLayoutResult.size.width / 2f,
            y = center.y - textLayoutResult.size.height / 2f,
        )
    )
}

private fun Float.toSp(): androidx.compose.ui.unit.TextUnit {
    return (this / 3f).sp
}
