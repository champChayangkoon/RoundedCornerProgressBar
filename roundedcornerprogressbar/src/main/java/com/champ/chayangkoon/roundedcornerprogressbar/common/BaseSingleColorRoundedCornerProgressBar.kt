package com.champ.chayangkoon.roundedcornerprogressbar.common

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.champ.chayangkoon.roundedcornerprogressbar.R

abstract class BaseSingleColorRoundedCornerProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseRoundedCornerProgressBar(context, attrs, defStyleAttr) {
    private var mBackgroundColor: Int = 0
    private var mProgressColor: Int = 0
    private var mSecondaryProgressColor: Int = 0

    var backgroundProgressColor: Int
        get() = mBackgroundColor
        set(value) {
            mBackgroundColor = value
        }

    var progressColor: Int
        get() = mProgressColor
        set(value) {
            mProgressColor = value
        }

    var secondaryProgressColor: Int
        get() = mSecondaryProgressColor
        set(value) {
            mSecondaryProgressColor = value
        }

    override fun AttributeSet.setupStyleable() {
        val typedArray = mContext.obtainStyledAttributes(this, R.styleable.BaseSingleColorRoundedCornerProgressBar)
        typedArray.let {
            backgroundProgressColor = it.getColor(
                    R.styleable.BaseSingleColorRoundedCornerProgressBar_rcpBackgroundColor,
                    getColor(R.color.colorBackgroundProgress)
            )
            progressColor = it.getColor(
                    R.styleable.BaseSingleColorRoundedCornerProgressBar_rcpProgressColor,
                    getColor(R.color.colorProgress)
            )
            secondaryProgressColor = it.getInt(
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


    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return superState?.let {
            val savedState = SavedState(it)
            savedState.mBackgroundColor = mBackgroundColor
            savedState.mProgressColor = mProgressColor
            savedState.mSecondaryProgressColor = mSecondaryProgressColor
            savedState
        } ?: superState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state?.let {
            if (it is SavedState) {
                super.onRestoreInstanceState(it.superState)
                mBackgroundColor = it.mBackgroundColor
                mProgressColor = it.mProgressColor
                mSecondaryProgressColor = it.mSecondaryProgressColor

            } else super.onRestoreInstanceState(state)
        } ?: super.onRestoreInstanceState(state)
    }

    private class SavedState : BaseSavedState {
        var mBackgroundColor: Int = 0
        var mProgressColor: Int = 0
        var mSecondaryProgressColor: Int = 0

        constructor(source: Parcel?) : super(source) {
            source?.let {
                mBackgroundColor = it.readInt()
                mProgressColor = it.readInt()
                mSecondaryProgressColor = it.readInt()
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        constructor(source: Parcel?, loader: ClassLoader?) : super(source, loader)
        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.let {
                it.writeInt(mBackgroundColor)
                it.writeInt(mProgressColor)
                it.writeInt(mSecondaryProgressColor)
            }
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(source: Parcel?) = SavedState(source)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }
}