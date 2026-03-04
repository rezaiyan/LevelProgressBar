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
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

private const val START_ANGLE = 120f
private const val TOTAL_ANGLE = 300f
private const val STEP_ARC_SWEEP = 10f
private const val STEP_ARC_GAP = 30f
private const val DISABLED_ALPHA = 0.4f

/**
 * The visual mode for rendering progress arcs.
 */
public enum class ProgressMode {
    /** A single continuous arc that fills proportionally. */
    Continuous,

    /** Discrete segmented steps around the arc. */
    Step,
}

/**
 * Color configuration for [LevelProgressBar].
 *
 * Use [LevelProgressBarDefaults.colors] to create instances.
 */
@Immutable
public data class LevelProgressBarColors(
    public val progressColor: Color,
    public val trackColor: Color,
    public val backgroundColor: Color,
    public val textColor: Color,
) {
    /**
     * Returns a copy with colors adjusted for the [enabled] state.
     */
    @Stable
    internal fun forState(enabled: Boolean): ResolvedColors {
        val alpha = if (enabled) 1f else DISABLED_ALPHA
        return ResolvedColors(
            progressColor = progressColor,
            trackColor = trackColor,
            backgroundColor = backgroundColor,
            textColor = textColor,
            alpha = alpha,
        )
    }
}

@Immutable
internal data class ResolvedColors(
    val progressColor: Color,
    val trackColor: Color,
    val backgroundColor: Color,
    val textColor: Color,
    val alpha: Float,
)

/**
 * Default values and factory methods for [LevelProgressBar].
 */
public object LevelProgressBarDefaults {

    /** Default maximum level. */
    public const val MaxLevel: Int = 10

    /** Default arc stroke width. */
    public val StrokeWidth: Dp = 10.dp

    /** Default component size. */
    public val Size: Dp = 250.dp

    /** Default progress mode. */
    public val Mode: ProgressMode = ProgressMode.Continuous

    /**
     * Creates a [LevelProgressBarColors] with the given values.
     *
     * @param progressColor Color of the filled progress arc.
     * @param trackColor Color of the unfilled track arc.
     * @param backgroundColor Color of the inner background circle.
     * @param textColor Color of the level number text.
     */
    @Composable
    public fun colors(
        progressColor: Color = Color.Green,
        trackColor: Color = Color.Gray,
        backgroundColor: Color = progressColor,
        textColor: Color = Color.White,
    ): LevelProgressBarColors = LevelProgressBarColors(
        progressColor = progressColor,
        trackColor = trackColor,
        backgroundColor = backgroundColor,
        textColor = textColor,
    )

    /**
     * The default animation spec used when [animated] is true.
     */
    public fun animationSpec(): AnimationSpec<Float> = tween(
        durationMillis = 1500,
        easing = FastOutSlowInEasing,
    )
}

/**
 * A circular progress bar that displays a level within a range.
 *
 * Supports two visual modes:
 * - [ProgressMode.Continuous]: A single arc that fills proportionally.
 * - [ProgressMode.Step]: Discrete segmented arcs around the circle.
 *
 * The center of the circle displays the current level number by default.
 * Provide [content] to replace the default text with custom content
 * (e.g., an icon or image).
 *
 * @param level Current progress level, clamped to `0..maxLevel`.
 * @param modifier Modifier for the root layout.
 * @param maxLevel Maximum level (inclusive). Must be > 0.
 * @param colors Color configuration. Use [LevelProgressBarDefaults.colors].
 * @param strokeWidth Width of the progress arc stroke.
 * @param mode Visual rendering mode (continuous arc or discrete steps).
 * @param enabled Whether the component is enabled. Disabled state reduces alpha.
 * @param animated Whether level changes animate or snap immediately.
 * @param animationSpec Animation spec used when [animated] is true.
 * @param content Optional composable slot for custom center content.
 *   When null, the level number is drawn as text.
 */
@Composable
public fun LevelProgressBar(
    level: Int,
    modifier: Modifier = Modifier,
    maxLevel: Int = LevelProgressBarDefaults.MaxLevel,
    colors: LevelProgressBarColors = LevelProgressBarDefaults.colors(),
    strokeWidth: Dp = LevelProgressBarDefaults.StrokeWidth,
    mode: ProgressMode = LevelProgressBarDefaults.Mode,
    enabled: Boolean = true,
    animated: Boolean = true,
    animationSpec: AnimationSpec<Float> = LevelProgressBarDefaults.animationSpec(),
    content: (@Composable (level: Int) -> Unit)? = null,
) {
    require(maxLevel > 0) { "maxLevel must be > 0, was $maxLevel" }

    val coercedLevel = level.coerceIn(0, maxLevel)
    val targetProgress = coercedLevel.toFloat() / maxLevel.toFloat()

    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(targetProgress) {
        if (animated) {
            animatedProgress.animateTo(
                targetValue = targetProgress,
                animationSpec = animationSpec,
            )
        } else {
            animatedProgress.snapTo(targetProgress)
        }
    }

    val displayLevel = if (animated) {
        (animatedProgress.value * maxLevel).toInt()
    } else {
        coercedLevel
    }

    val resolved = colors.forState(enabled)
    val modeLabel = when (mode) {
        ProgressMode.Step -> "step"
        ProgressMode.Continuous -> "continuous"
    }

    Box(
        modifier = modifier
            .size(LevelProgressBarDefaults.Size)
            .aspectRatio(1f)
            .testTag("LevelProgressBar")
            .semantics {
                contentDescription = "Level $displayLevel of $maxLevel"
                stateDescription = if (enabled) modeLabel else "disabled"
                progressBarRangeInfo = androidx.compose.ui.semantics.ProgressBarRangeInfo(
                    current = coercedLevel.toFloat(),
                    range = 0f..maxLevel.toFloat(),
                    steps = maxLevel - 1,
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        val textMeasurer = rememberTextMeasurer()

        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val canvasSize = min(size.width, size.height)
            val radius = canvasSize / 2.5f
            val center = Offset(size.width / 2f, size.height / 2f)

            val arcRect = Size(
                canvasSize - strokeWidthPx * 2,
                canvasSize - strokeWidthPx * 2,
            )
            val arcTopLeft = Offset(
                (size.width - arcRect.width) / 2f,
                (size.height - arcRect.height) / 2f,
            )

            drawCircle(
                color = resolved.backgroundColor,
                radius = radius - strokeWidthPx,
                center = center,
                alpha = resolved.alpha,
            )

            val currentAngle = TOTAL_ANGLE * animatedProgress.value

            when (mode) {
                ProgressMode.Step -> drawStepProgress(
                    arcTopLeft = arcTopLeft,
                    arcSize = arcRect,
                    currentAngle = currentAngle,
                    strokeWidthPx = strokeWidthPx,
                    progressColor = resolved.progressColor,
                    trackColor = resolved.trackColor,
                    alpha = resolved.alpha,
                )
                ProgressMode.Continuous -> drawContinuousProgress(
                    arcTopLeft = arcTopLeft,
                    arcSize = arcRect,
                    currentAngle = currentAngle,
                    strokeWidthPx = strokeWidthPx,
                    progressColor = resolved.progressColor,
                    trackColor = resolved.trackColor,
                    alpha = resolved.alpha,
                )
            }

            if (content == null) {
                drawLevelText(
                    textMeasurer = textMeasurer,
                    level = displayLevel,
                    center = center,
                    radius = radius,
                    textColor = resolved.textColor,
                    alpha = resolved.alpha,
                )
            }
        }

        if (content != null) {
            content(displayLevel)
        }
    }
}

/**
 * Backward-compatible overload using individual color parameters.
 *
 * Prefer the primary overload with [LevelProgressBarColors] for new code.
 */
@Composable
public fun LevelProgressBar(
    level: Int,
    modifier: Modifier = Modifier,
    maxLevel: Int = LevelProgressBarDefaults.MaxLevel,
    progressColor: Color = Color.Green,
    unProgressColor: Color = Color.Gray,
    backgroundColor: Color = progressColor,
    textColor: Color = Color.White,
    strokeWidth: Dp = LevelProgressBarDefaults.StrokeWidth,
    isStepProgress: Boolean = false,
    enabled: Boolean = true,
    @Suppress("UNUSED_PARAMETER") imageBitmap: androidx.compose.ui.graphics.ImageBitmap? = null,
    animated: Boolean = true,
) {
    LevelProgressBar(
        level = level,
        modifier = modifier,
        maxLevel = maxLevel,
        colors = LevelProgressBarColors(
            progressColor = progressColor,
            trackColor = unProgressColor,
            backgroundColor = backgroundColor,
            textColor = textColor,
        ),
        strokeWidth = strokeWidth,
        mode = if (isStepProgress) ProgressMode.Step else ProgressMode.Continuous,
        enabled = enabled,
        animated = animated,
    )
}

// ---------------------------------------------------------------------------
// Internal drawing helpers
// ---------------------------------------------------------------------------

private fun DrawScope.drawContinuousProgress(
    arcTopLeft: Offset,
    arcSize: Size,
    currentAngle: Float,
    strokeWidthPx: Float,
    progressColor: Color,
    trackColor: Color,
    alpha: Float,
) {
    if (currentAngle > 0f) {
        drawArc(
            color = progressColor,
            startAngle = START_ANGLE,
            sweepAngle = currentAngle,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt),
            alpha = alpha,
        )
    }
    val remainingAngle = TOTAL_ANGLE - currentAngle
    if (remainingAngle > 0f) {
        drawArc(
            color = trackColor,
            startAngle = START_ANGLE + currentAngle,
            sweepAngle = remainingAngle,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt),
            alpha = alpha,
        )
    }
}

private fun DrawScope.drawStepProgress(
    arcTopLeft: Offset,
    arcSize: Size,
    currentAngle: Float,
    strokeWidthPx: Float,
    progressColor: Color,
    trackColor: Color,
    alpha: Float,
) {
    var step = STEP_ARC_SWEEP
    while (step <= TOTAL_ANGLE) {
        val color = if (step <= currentAngle) progressColor else trackColor
        drawArc(
            color = color,
            startAngle = START_ANGLE + step,
            sweepAngle = STEP_ARC_SWEEP,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
            alpha = alpha,
        )
        step += STEP_ARC_GAP
    }
}

private fun DrawScope.drawLevelText(
    textMeasurer: androidx.compose.ui.text.TextMeasurer,
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
        ),
    )
}

private fun Float.toSp(): androidx.compose.ui.unit.TextUnit {
    return (this / 3f).sp
}
