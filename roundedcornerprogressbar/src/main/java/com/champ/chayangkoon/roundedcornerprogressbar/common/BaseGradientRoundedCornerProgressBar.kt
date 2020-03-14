package com.champ.chayangkoon.roundedcornerprogressbar.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.ColorRes
import com.champ.chayangkoon.roundedcornerprogressbar.R
import java.lang.IllegalArgumentException

abstract class BaseGradientRoundedCornerProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseRoundedCornerProgressBar(context, attrs, defStyleAttr) {
    private var mBackgroundColors: IntArray = intArrayOf()
    private var mBackgroundColorsPosition: FloatArray = floatArrayOf()
    private var mBackgroundStartColor: Int = 0
    private var mBackgroundCenterColor: Int = 0
    private var mBackgroundEndColor: Int = 0

    private var mProgressColors: IntArray = intArrayOf()
    private var mProgressColorsPosition: FloatArray = floatArrayOf()
    private var mProgressStartColor: Int = 0
    private var mProgressCenterColor: Int = 0
    private var mProgressEndColor: Int = 0

    private var mSecondaryProgressColors: IntArray = intArrayOf()
    private var mSecondaryProgressColorsPosition: FloatArray = floatArrayOf()
    private var mSecondaryProgressStartColor: Int = 0
    private var mSecondaryProgressCenterColor: Int = 0
    private var mSecondaryProgressEndColor: Int = 0

    override fun AttributeSet.setupStyleable() {
        val typedArray = mContext.obtainStyledAttributes(this, R.styleable.BaseGradientRoundedCornerProgressBar)
        typedArray.let {
            mBackgroundStartColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpBackgroundStartColor,
                    getColor(R.color.colorProgress)
            )
            mBackgroundCenterColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpBackgroundCenterColor,
                    0
            )
            mBackgroundEndColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpBackgroundEndColor,
                    mBackgroundStartColor
            )
            mProgressStartColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpProgressStartColor,
                    getColor(R.color.colorProgress)
            )
            mProgressCenterColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpProgressCenterColor,
                    0
            )
            mProgressEndColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpProgressEndColor,
                    getColor(R.color.colorEndProgress)
            )
            mSecondaryProgressStartColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpSecondaryProgressStartColor,
                    getColor(R.color.colorSecondaryProgress)
            )
            mSecondaryProgressCenterColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpSecondaryProgressCenterColor,
                    0
            )
            mSecondaryProgressEndColor = it.getColor(
                    R.styleable.BaseGradientRoundedCornerProgressBar_rcpSecondaryProgressEndColor,
                    getColor(R.color.colorEndSecondaryProgress)
            )
        }
        typedArray.recycle()
    }


    override fun setupView() {}
    override fun setupBackgroundProgress(mBackgroundRect: RectF, mBackgroundPaint: Paint) {}
    override fun setupProgress(mProgressRect: RectF, mProgressPaint: Paint) {}
    override fun setupSecondaryProgress(mSecondaryProgressRect: RectF, mSecondaryProgressPaint: Paint) {}

    override fun onPreDrawBackground(mBackgroundRect: RectF, mBackgroundPaint: Paint) {
        if (mBackgroundColors.none() && mBackgroundColorsPosition.none()) {
            val isGradientColorsBetweenTwoColor = isGradientColorsBetweenTwoColor(
                    mBackgroundStartColor,
                    mBackgroundCenterColor,
                    mBackgroundEndColor
            )

            val isGradientColorsBetweenThreeColor = isGradientColorsBetweenThreeColor(
                    mBackgroundStartColor,
                    mBackgroundCenterColor,
                    mBackgroundEndColor
            )

            val isGradientColorsBetweenStartAndCenter = isGradientColorsBetweenStartAndCenter(
                    mBackgroundStartColor,
                    mBackgroundCenterColor,
                    mBackgroundEndColor
            )

            val isGradientColorsBetweenCenterAndEnd = isGradientColorsBetweenCenterAndEnd(
                    mBackgroundStartColor,
                    mBackgroundCenterColor,
                    mBackgroundEndColor
            )

            setGradientColors(
                    isGradientColorsBetweenTwoColor,
                    isGradientColorsBetweenThreeColor,
                    isGradientColorsBetweenStartAndCenter,
                    isGradientColorsBetweenCenterAndEnd,
                    mBackgroundRect,
                    mBackgroundPaint
            )
        } else if (mBackgroundColors.any() && mBackgroundColorsPosition.any()
                && mBackgroundColors.size == mBackgroundColorsPosition.size) {
            val linearGradient = getLinearGradientMultiColors(
                    mBackgroundRect,
                    mBackgroundColors,
                    mBackgroundColorsPosition)
            mBackgroundPaint.shader = linearGradient

        } else if (mBackgroundColors.none() || mBackgroundColorsPosition.none()) {
            throw IllegalArgumentException("background color or position arrays is empty")

        } else if (mBackgroundColors.size != mBackgroundColorsPosition.size) {
            throw IllegalArgumentException("background color and position arrays must be of equal length")
        }
    }

    override fun onPreDrawProgress(mProgressRect: RectF, mProgressPaint: Paint, isReverse: Boolean) {
        if (mProgressColors.none() && mProgressColorsPosition.none()) {
            val isGradientColorsBetweenTwoColor = isGradientColorsBetweenTwoColor(
                    mProgressStartColor,
                    mProgressCenterColor,
                    mProgressEndColor
            )

            val isGradientColorsBetweenThreeColor = isGradientColorsBetweenThreeColor(
                    mProgressStartColor,
                    mProgressCenterColor,
                    mProgressEndColor
            )

            val isGradientColorsBetweenStartAndCenter = isGradientColorsBetweenStartAndCenter(
                    mProgressStartColor,
                    mProgressCenterColor,
                    mProgressEndColor
            )

            val isGradientColorsBetweenCenterAndEnd = isGradientColorsBetweenCenterAndEnd(
                    mProgressStartColor,
                    mProgressCenterColor,
                    mProgressEndColor
            )

            setGradientColors(
                    isGradientColorsBetweenTwoColor,
                    isGradientColorsBetweenThreeColor,
                    isGradientColorsBetweenStartAndCenter,
                    isGradientColorsBetweenCenterAndEnd,
                    mProgressRect,
                    mProgressPaint
            )

        } else if (mProgressColors.any() && mProgressColorsPosition.any()
                && mProgressColors.size == mProgressColorsPosition.size) {

            val linearGradient = getLinearGradientMultiColors(
                    mProgressRect,
                    mProgressColors,
                    mProgressColorsPosition)
            mProgressPaint.shader = linearGradient

        } else if (mProgressColors.none() || mProgressColorsPosition.none()) {
            throw IllegalArgumentException("progress color or position arrays is empty")

        } else if (mProgressColors.size != mProgressColorsPosition.size) {
            throw IllegalArgumentException("progress color and position arrays must be of equal length")
        }
    }

    override fun onPreDrawSecondaryProgress(mSecondaryProgressRect: RectF, mSecondaryProgressPaint: Paint) {
        if (mSecondaryProgressColors.none() && mSecondaryProgressColorsPosition.none()) {
            val isGradientColorsBetweenTwoColor = isGradientColorsBetweenTwoColor(
                    mSecondaryProgressStartColor,
                    mSecondaryProgressCenterColor,
                    mSecondaryProgressEndColor
            )

            val isGradientColorsBetweenThreeColor = isGradientColorsBetweenThreeColor(
                    mSecondaryProgressStartColor,
                    mSecondaryProgressCenterColor,
                    mSecondaryProgressEndColor
            )

            val isGradientColorsBetweenStartAndCenter = isGradientColorsBetweenStartAndCenter(
                    mSecondaryProgressStartColor,
                    mSecondaryProgressCenterColor,
                    mSecondaryProgressEndColor
            )

            val isGradientColorsBetweenCenterAndEnd = isGradientColorsBetweenCenterAndEnd(
                    mSecondaryProgressStartColor,
                    mSecondaryProgressCenterColor,
                    mSecondaryProgressEndColor
            )

            setGradientColors(
                    isGradientColorsBetweenTwoColor,
                    isGradientColorsBetweenThreeColor,
                    isGradientColorsBetweenStartAndCenter,
                    isGradientColorsBetweenCenterAndEnd,
                    mSecondaryProgressRect,
                    mSecondaryProgressPaint
            )
        } else if (mSecondaryProgressColors.any() && mSecondaryProgressColorsPosition.any()) {
            val linearGradient = getLinearGradientMultiColors(
                    mSecondaryProgressRect,
                    mSecondaryProgressColors,
                    mSecondaryProgressColorsPosition)
            mSecondaryProgressPaint.shader = linearGradient

        } else if (mSecondaryProgressColors.none() || mSecondaryProgressColorsPosition.none()) {
            throw IllegalArgumentException("secondary progress color or position arrays is empty")

        } else if (mSecondaryProgressColors.size != mSecondaryProgressColorsPosition.size) {
            throw IllegalArgumentException("secondary progress color and position arrays must be of equal length")
        }
    }

    private fun isGradientColorsBetweenTwoColor(startColor: Int, centerColor: Int, endColor: Int): Boolean {
        return startColor != 0 && endColor != 0 && centerColor == 0
    }

    private fun isGradientColorsBetweenThreeColor(startColor: Int, centerColor: Int, endColor: Int): Boolean {
        return startColor != 0 && endColor != 0 && centerColor != 0
    }

    private fun isGradientColorsBetweenStartAndCenter(startColor: Int, centerColor: Int, endColor: Int): Boolean {
        return startColor != 0 && centerColor != 0 && endColor == 0
    }

    private fun isGradientColorsBetweenCenterAndEnd(startColor: Int, centerColor: Int, endColor: Int): Boolean {
        return startColor == 0 && centerColor != 0 && endColor != 0
    }

    private fun setGradientColors(
            isGradientColorsBetweenTwoColor: Boolean,
            isGradientColorsBetweenThreeColor: Boolean,
            isGradientColorsBetweenStartAndCenter: Boolean,
            isGradientColorsBetweenCenterAndEnd: Boolean,
            rectF: RectF,
            paint: Paint
    ) {
        when {
            isGradientColorsBetweenTwoColor -> {
                paint.shader = getLinearGradientBetweenTwoColors(rectF, mProgressStartColor, mProgressEndColor)
            }
            isGradientColorsBetweenThreeColor -> {
                paint.shader = getLinearGradientBetweenThreeColors(
                        rectF,
                        mProgressStartColor,
                        mProgressCenterColor,
                        mProgressEndColor
                )
            }
            isGradientColorsBetweenStartAndCenter -> {
                paint.shader = getLinearGradientBetweenThreeColors(
                        rectF,
                        mProgressStartColor,
                        mProgressCenterColor,
                        mProgressStartColor
                )
            }
            isGradientColorsBetweenCenterAndEnd -> {
                paint.shader = getLinearGradientBetweenThreeColors(
                        rectF,
                        mProgressEndColor,
                        mProgressCenterColor,
                        mProgressEndColor
                )
            }
        }
    }

    private fun getLinearGradientBetweenTwoColors(
            rectF: RectF,
            startColor: Int,
            endColor: Int
    ): LinearGradient {
        return LinearGradient(
                rectF.left,
                0f,
                rectF.right,
                rectF.bottom,
                startColor,
                endColor,
                Shader.TileMode.CLAMP
        )
    }

    private fun getLinearGradientBetweenThreeColors(
            rectF: RectF,
            startColor: Int,
            centerColor: Int,
            endColor: Int
    ): LinearGradient {
        return LinearGradient(
                rectF.left,
                0f,
                rectF.right,
                rectF.bottom,
                intArrayOf(startColor, centerColor, endColor),
                floatArrayOf(.0f, .5f, 1f),
                Shader.TileMode.CLAMP
        )
    }

    private fun getLinearGradientMultiColors(
            rectF: RectF,
            colors: IntArray,
            colorsPosition: FloatArray
    ): LinearGradient {
        return LinearGradient(
                rectF.left,
                0f,
                rectF.right,
                rectF.bottom,
                colors,
                colorsPosition,
                Shader.TileMode.REPEAT
        )
    }

    fun setBackgroundColors(colors: IntArray, colorsPosition: FloatArray) {
        mBackgroundColors = colors
        mBackgroundColorsPosition = colorsPosition
    }

    fun setBackgroundColors(
            @ColorRes startColor: Int,
            @ColorRes endColor: Int = startColor,
            @ColorRes centerColor: Int = 0
    ) {
        mBackgroundStartColor = getColor(startColor)
        mBackgroundCenterColor = getColor(centerColor)
        mBackgroundEndColor = getColor(endColor)
    }

    fun setProgressColors(colors: IntArray, colorsPosition: FloatArray) {
        mProgressColors = colors
        mProgressColorsPosition = colorsPosition
    }

    fun setProgressColors(
            @ColorRes startColor: Int,
            @ColorRes endColor: Int,
            @ColorRes centerColor: Int = 0
    ) {
        mProgressStartColor = getColor(startColor)
        mProgressCenterColor = getColor(centerColor)
        mProgressEndColor = getColor(endColor)
    }

    fun setSecondaryProgressColors(colors: IntArray, colorsPosition: FloatArray) {
        mSecondaryProgressColors = colors
        mSecondaryProgressColorsPosition = colorsPosition
    }

    fun setSecondaryProgressColors(
            @ColorRes startColor: Int,
            @ColorRes endColor: Int,
            @ColorRes centerColor: Int = 0
    ) {
        mSecondaryProgressStartColor = getColor(startColor)
        mSecondaryProgressCenterColor = getColor(centerColor)
        mSecondaryProgressEndColor = getColor(endColor)
    }
}