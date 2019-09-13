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

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import ir.alirezaiyan.progressbar.utils.getBoundRectF
import ir.alirezaiyan.progressbar.utils.getColor
import kotlin.math.min


/**
 * @author ali (alirezaiyann@gmail.com)
 * @since 8/25/19 10:29 PM.
 */

class LevelProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {

        const val DEFAULT_SPEED = 1F
        const val DEFAULT_TEXT_TITLE = "Level"
        const val DEFAULT_START_ANGLE = 120
        const val DEFAULT_TOTAL_ANGLE = 300
        const val DEFAULT_STROKE_WiDTH = 10
        const val OPACITY = 100
        const val DEFAULT_MAX_PROGRESS = 100
        const val DEFAULT_TEXT_LEVEL_COLOR = Color.WHITE
        const val DEFAULT_TEXT_TITLE_COLOR = Color.BLACK
        const val DEFAULT_BACKGROUND_COLOR = Color.GREEN
        const val DEFAULT_UNPROGRESS_COLOR = Color.GRAY
        const val DEFAULT_IS_ENABLE = true
        const val DEFAULT_IS_STEP_PROGRESS = false

    }

    private var continuousSwipeAngle = 0f
    private var continuousStartAngle = 0f
    private var speed = DEFAULT_SPEED
    private var progress = 0
    private var angle = 0F
    private var textOffset = 0F

    private var textTitle: String? = DEFAULT_TEXT_TITLE

    private var textLevelColor = DEFAULT_TEXT_LEVEL_COLOR
    private var textTitleColor = DEFAULT_TEXT_TITLE_COLOR
    private var progressColor = DEFAULT_BACKGROUND_COLOR
    private var unprogressColor = DEFAULT_UNPROGRESS_COLOR

    private var isEnable = DEFAULT_IS_ENABLE
    private var isStepProgress = DEFAULT_IS_STEP_PROGRESS

    private var mReady: Boolean = false
    private var mSetupPending: Boolean = false

    private lateinit var bitmapShader: BitmapShader
    private var bitmap: Bitmap? = null
    private val mShaderMatrix = Matrix()
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0
    private val drawableRect = RectF()

    private var radius = 50
    var strokeWidth = DEFAULT_STROKE_WiDTH.toFloat()
        set(value) {
            if (value < radius / 3)
                field = value

            setup()
            invalidate()
        }

    private val borderRect = RectF()

    private var progressPaint = Paint()
    private var unProgressPaint = Paint()
    private var textLevelPaint = Paint()
    private var textTitlePaint = Paint()
    private val backgroundPaint = Paint()


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SpeedProgressBar,
            0, 0
        ).apply {
            try {
                speed = getFloat(
                    R.styleable.SpeedProgressBar_spb_level,
                    DEFAULT_SPEED
                )

                textTitle = getString(R.styleable.SpeedProgressBar_spb_text_title)

                isEnable = getBoolean(
                    R.styleable.SpeedProgressBar_spb_is_enable,
                    DEFAULT_IS_ENABLE
                )

                isStepProgress = getBoolean(
                    R.styleable.SpeedProgressBar_spb_is_step_progress,
                    DEFAULT_IS_ENABLE
                )

                textLevelColor = getColor(
                    R.styleable.SpeedProgressBar_spb_text_level_color,
                    DEFAULT_TEXT_LEVEL_COLOR
                )

                textTitleColor = getColor(
                    R.styleable.SpeedProgressBar_spb_text_title_color,
                    DEFAULT_TEXT_TITLE_COLOR
                )

                progressColor = getColor(
                    R.styleable.SpeedProgressBar_spb_background_color,
                    DEFAULT_TEXT_TITLE_COLOR
                )

                unprogressColor = getColor(
                    R.styleable.SpeedProgressBar_spb_unprogress_color,
                    DEFAULT_UNPROGRESS_COLOR
                )

                strokeWidth = getInteger(
                    R.styleable.SpeedProgressBar_spb_stroke_with,
                    DEFAULT_STROKE_WiDTH
                ).toFloat()

                getDrawable(R.styleable.SpeedProgressBar_spb_src)?.let {
                    bitmap = (it as BitmapDrawable).bitmap
                    progressColor = bitmap.getColor()
                }

            } finally {
                recycle()
            }
        }

        mReady = true

        if (mSetupPending) {
            setup()
            mSetupPending = false
        }

    }

    private fun setup() {

        if (!mReady) {
            mSetupPending = true
            return
        }

        if (width == 0 && height == 0) {
            return
        }

        borderRect.set(getBoundRectF(strokeWidth))

        textLevelPaint.apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            textSize = radius.toFloat()
            color = textLevelColor
        }

        textTitlePaint.apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            textSize = radius.toFloat()
            color = textTitleColor
        }

        progressPaint.apply {
            isAntiAlias = true
            color = ColorUtils.blendARGB(progressColor, Color.BLACK, 0.2f)
            style = Paint.Style.STROKE
            strokeWidth = this@LevelProgressBar.strokeWidth
            strokeCap = if (isStepProgress) Paint.Cap.ROUND else Paint.Cap.BUTT
        }

        unProgressPaint.apply {
            isAntiAlias = true
            color = unprogressColor
            style = Paint.Style.STROKE
            strokeWidth = this@LevelProgressBar.strokeWidth
            strokeCap = if (isStepProgress) Paint.Cap.ROUND else Paint.Cap.BUTT

        }

        backgroundPaint.apply {
            isAntiAlias = true
            color = progressColor
        }

        if (!isEnable) {
            textLevelPaint.alpha = OPACITY
            textTitlePaint.alpha = OPACITY
            progressPaint.alpha = OPACITY
            backgroundPaint.alpha = OPACITY
            unProgressPaint.alpha = OPACITY
        }

        progress = (speed * 10).toInt()
        angle = (DEFAULT_TOTAL_ANGLE * progress / DEFAULT_MAX_PROGRESS).toFloat()

        val textHeight = textLevelPaint.descent() - textLevelPaint.ascent()
        textOffset = textHeight / 2 - textLevelPaint.descent()
        continuousSwipeAngle = DEFAULT_START_ANGLE + angle
        continuousStartAngle = DEFAULT_TOTAL_ANGLE - angle

        bitmap?.let {

            bitmapHeight = it.height
            bitmapWidth = it.width

            bitmapShader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            backgroundPaint.shader = bitmapShader

            drawableRect.set(borderRect)

            updateShaderMatrix()

        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(
            borderRect.centerX(),
            borderRect.centerY(),
            radius - strokeWidth,
            backgroundPaint
        )


        //    step progress
        if (isStepProgress) {
            var step = 10
            while (step <= DEFAULT_TOTAL_ANGLE) {
                if (step <= angle) {
                    canvas.drawArc(
                        borderRect,
                        (DEFAULT_START_ANGLE + step).toFloat(),
                        10f,
                        false,
                        progressPaint
                    )
                } else {
                    canvas.drawArc(
                        borderRect,
                        (DEFAULT_START_ANGLE + step).toFloat(),
                        10f,
                        false,
                        unProgressPaint
                    )
                }
                step += 30
            }
        } else {


            canvas.drawArc(borderRect, DEFAULT_START_ANGLE.toFloat(), angle, false, progressPaint)

            if (angle < DEFAULT_TOTAL_ANGLE) {

                canvas.drawArc(
                    borderRect,
                    continuousSwipeAngle,
                    continuousStartAngle,
                    false,
                    unProgressPaint
                )
            }
        }


        //levelTitle
        if (bitmap == null) {
            canvas
                .drawText(
                    speed.toInt().toString(),
                    borderRect.centerX(),
                    borderRect.centerY() + textOffset,
                    textLevelPaint
                )
        }


    }


    /*Don't set background*/
    override fun setBackground(background: Drawable) {
        //    super.setBackground(background);
    }

    override fun setBackgroundColor(color: Int) {
        //    super.setBackgroundColor(color);
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (isEnable) {
            super.setOnClickListener(l)
        }
    }

    override fun setOnTouchListener(l: OnTouchListener) {
        if (isEnable) {
            super.setOnTouchListener(l)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = (width + strokeWidth + paddingRight + paddingLeft).toInt()
        val desiredHeight = (height + strokeWidth + paddingTop + paddingBottom).toInt()

        val width: Int
        val height: Int

        //Measure Width
        width = when (widthMode) {
            MeasureSpec.EXACTLY -> //Must be this size
                widthSize
            MeasureSpec.AT_MOST -> //Can't be bigger than...
                min(desiredWidth, widthSize)
            else -> //Be whatever you want
                desiredWidth
        }

        //Measure Height
        height = when (heightMode) {
            MeasureSpec.EXACTLY -> //Must be this size
                heightSize
            MeasureSpec.AT_MOST -> //Can't be bigger than...
                min(desiredHeight, heightSize)
            else -> //Be whatever you want
                desiredHeight
        }
        val min = min(width, height)
        radius = (min / 2.5).toInt()

        setMeasuredDimension(width, height)

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }


    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight.toFloat()
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = drawableRect.width() / bitmapWidth.toFloat()
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(
            (dx + 0.5f).toInt() + drawableRect.left,
            (dy + 0.5f).toInt() + drawableRect.top
        )

        bitmapShader.setLocalMatrix(mShaderMatrix)
    }


    fun setProgressWithAnimation(progress: Float) {

        val objectAnimator = ObjectAnimator.ofFloat(this, "speed", progress)
        objectAnimator.duration = 1500
        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.start()
    }

    //<editor-fold desc="Setter/Getter">

    fun setTypeface(typeface: Typeface) {
        textTitlePaint.typeface = typeface
        textLevelPaint.typeface = typeface
    }

    fun getSpeed(): Float {
        return speed
    }

    fun setSpeed(speed: Float) {

        if (this.speed == speed) {
            return
        }

        this.speed = speed
        setup()
        invalidate()
    }


    fun getTextTitle(): String? {
        return textTitle
    }

    fun setTextTitle(textTitle: String) {

        if (this.textTitle == textTitle) {
            return
        }

        this.textTitle = textTitle
        setup()
    }

    fun getTextLevelColor(): Int {
        return textLevelColor
    }

    fun setTextLevelColor(textLevelColor: Int) {

        if (this.textLevelColor == textLevelColor) {
            return
        }

        this.textLevelColor = textLevelColor
        setup()
    }

    fun getTextTitleColor(): Int {
        return textTitleColor
    }

    fun setTextTitleColor(textTitleColor: Int) {

        if (this.textTitleColor == textTitleColor) {
            return
        }

        this.textTitleColor = textTitleColor
        setup()

    }

    fun getBackgroundProgressColor(): Int {
        return progressColor
    }

    fun setBackgroundProgressColor(backgroundProgressColor: Int) {

        this.bitmap = null
        this.progressColor = backgroundProgressColor
        setup()
        invalidate()
    }

    fun getUnprogressColor(): Int {
        return unprogressColor
    }

    fun setUnprogressColor(unprogressColor: Int) {

        if (this.unprogressColor == unprogressColor) {
            return
        }

        this.unprogressColor = unprogressColor
        setup()

    }

    fun isEnable(): Boolean {
        return isEnable
    }

    fun setEnable(enable: Boolean) {
        if (this.isEnable == enable) {
            return
        }

        this.isEnable = enable
        setup()
        requestLayout()
    }


    fun setIsStep(isStepProgress: Boolean) {
        if (this.isStepProgress == isStepProgress) {
            return
        }

        this.isStepProgress = isStepProgress
        setup()
        invalidate()
    }


    fun setImage(drawable: Int) {
        this.bitmap = (ContextCompat.getDrawable(context!!, drawable) as BitmapDrawable).bitmap
        setup()
        requestLayout()
    }

    //</editor-fold>

}