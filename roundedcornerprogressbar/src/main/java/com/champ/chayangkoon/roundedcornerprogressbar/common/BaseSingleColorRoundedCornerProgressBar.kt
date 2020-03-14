package com.champ.chayangkoon.roundedcornerprogressbar.common

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.champ.chayangkoon.roundedcornerprogressbar.R

abstract class BaseSingleColorRoundedCornerProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseRoundedCornerProgressBar(context, attrs, defStyleAttr) {
    private var mBackgroundColor: Int = 0
    private var mProgressColor: Int = 0
    private var mSecondaryProgressColor: Int = 0

    override fun AttributeSet.setupStyleable() {
        val typedArray = mContext.obtainStyledAttributes(this, R.styleable.BaseSingleColorRoundedCornerProgressBar)
        typedArray.let {
            mBackgroundColor = it.getColor(
                    R.styleable.BaseSingleColorRoundedCornerProgressBar_rcpBackgroundColor,
                    getColor(R.color.colorBackgroundProgress)
            )
            mProgressColor = it.getColor(
                    R.styleable.BaseSingleColorRoundedCornerProgressBar_rcpProgressColor,
                    getColor(R.color.colorProgress)
            )
            mSecondaryProgressColor = it.getInt(
                    R.styleable.BaseSingleColorRoundedCornerProgressBar_rcpSecondaryProgressColor,
                    getColor(R.color.colorSecondaryProgress)
            )
        }
        typedArray.recycle()
    }

    override fun setupView() {}

    override fun setupBackgroundProgress(mBackgroundRect: RectF, mBackgroundPaint: Paint) {
        mBackgroundPaint.color = mBackgroundColor
    }

    override fun setupProgress(mProgressRect: RectF, mProgressPaint: Paint) {
        mProgressPaint.color = mProgressColor
    }

    override fun setupSecondaryProgress(mSecondaryProgressRect: RectF, mSecondaryProgressPaint: Paint) {
        mSecondaryProgressPaint.color = mSecondaryProgressColor
    }
}