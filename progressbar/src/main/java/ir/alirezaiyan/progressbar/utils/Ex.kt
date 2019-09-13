package ir.alirezaiyan.progressbar.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.RectF
import android.view.View
import kotlin.math.min

/**
 * @author ali (alirezaiyann@gmail.com)
 * @since 9/13/19 1:24 PM.
 */


fun Bitmap?.getColor(): Int {

    if (null == this) return Color.TRANSPARENT

    var redBucket = 0
    var greenBucket = 0
    var blueBucket = 0
    var alphaBucket = 0

    val hasAlpha = hasAlpha()
    val pixelCount = width * height
    val pixels = IntArray(pixelCount)
    getPixels(pixels, 0, width, 0, 0, width, height)

    var y = 0
    val h = height
    while (y < h) {
        var x = 0
        val w = width
        while (x < w) {
            val color = pixels[x + y * w] // x + y * width
            redBucket += color shr 16 and 0xFF // Color.red
            greenBucket += color shr 8 and 0xFF // Color.greed
            blueBucket += color and 0xFF // Color.blue
            if (hasAlpha) alphaBucket += color.ushr(24) // Color.alpha
            x++
        }
        y++
    }

    return Color.argb(
        if (hasAlpha) alphaBucket / pixelCount else 255,
        redBucket / pixelCount,
        greenBucket / pixelCount,
        blueBucket / pixelCount
    )

}

fun View.getBoundRectF(strokeWidth: Float): RectF {

    val availableWidth = width - paddingLeft - paddingRight
    val availableHeight = height - paddingTop - paddingBottom

    val sideLength = min(availableWidth, availableHeight)

    val left = (paddingLeft + (availableWidth - sideLength))
    val top = (paddingTop + (availableHeight - sideLength))

    return RectF(
        left + strokeWidth,
        top + strokeWidth,
        left + sideLength - strokeWidth,
        top + sideLength - strokeWidth
    )

}