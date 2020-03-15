package com.champ.chayangkoon.roundedcornerprogressbar.common

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorRes
import androidx.annotation.InterpolatorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.champ.chayangkoon.roundedcornerprogressbar.R

abstract class BaseRoundedCornerProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    protected val mContext = context
    private var mAnimateDuration = PROGRESS_ANIM_DURATION
    private var mAnimatorListener: Animator.AnimatorListener? = null
    private var mAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null
    private var mAnimatorPauseListener: Animator.AnimatorPauseListener? = null
    private var mIndeterminate: Boolean = false
    private var mInterpolator: TimeInterpolator = LinearInterpolator()
    private var mInterpolatorResId: Int = 0
    private var mIsReverse: Boolean = false
    private var mMax: Int = MAX_PROGRESS
    private var mMaxInitialized: Boolean = false
    private var mMin: Int = 0
    private var mMinInitialized: Boolean = false
    private var mRadius: Float = 0f
    private var mRoundedWidth = 0f
    private var mBackgroundPadding: Float = 0f
    private var mProgress: Int = 0
    private var mProgressAnimator: ObjectAnimator? = null
    private var mSecondaryProgress: Int = 0
    private var mCurrentPlayTime: Long = 0

    private lateinit var mBackgroundRect: RectF
    private lateinit var mBackgroundRectWithPadding: RectF
    private lateinit var mBackgroundPaint: Paint
    private lateinit var mProgressRect: RectF
    private lateinit var mProgressPaint: Paint
    private lateinit var mSecondaryProgressRect: RectF
    private lateinit var mSecondaryProgressPaint: Paint

    var animateDuration: Long
        get() = mAnimateDuration
        set(value) {
            mAnimateDuration = value
        }

    var isReverse: Boolean
        get() = mIsReverse
        set(value) {
            mIsReverse = value
        }

    var interpolator: TimeInterpolator
        get() = mInterpolator
        set(value) {
            mInterpolator = value
        }

    var indeterminate: Boolean
        get() = mIndeterminate
        set(value) {
            mIndeterminate = value
        }

    var max: Int
        get() = mMax
        set(value) {
            setupMax(value)
        }

    private fun setupMax(max: Int) {
        var maxTemp = max
        if (mMinInitialized) {
            if (maxTemp < mMin) {
                maxTemp = mMin
            }
        }
        mMaxInitialized = true
        if (mMinInitialized && maxTemp != mMax) {
            mMax = maxTemp
            if (mProgress > maxTemp) {
                mProgress = maxTemp
            }
        } else {
            mMax = maxTemp
        }
    }

    var min: Int
        get() = mMin
        set(value) {
            setupMin(value)
        }

    private fun setupMin(min: Int) {
        var minTemp = min
        if (mMaxInitialized) {
            if (minTemp > mMax) {
                minTemp = mMax
            }
        }
        mMinInitialized = true
        if (mMaxInitialized && minTemp != mMin) {
            mMin = minTemp
            if (mProgress < minTemp) {
                mProgress = minTemp
            }
        } else {
            mMin = minTemp
        }
    }

    var backgroundPadding: Float
        get() = mBackgroundPadding
        set(value) {
            mBackgroundPadding = value
        }

    var progress: Int
        get() = mProgress
        set(value) {
            if (mIndeterminate) return
            var progressTemp = value
            if (progressTemp < mMin) progressTemp = mMin
            if (progressTemp > mMax) progressTemp = mMax

            if (progressTemp != mProgress) {
                mProgress = progressTemp
                postInvalidateOnAnimation()
            }

        }

    var radius: Float
        get() = mRadius
        set(value) {
            mRadius = value
            mRoundedWidth = value * 2
        }

    var secondaryProgress: Int
        get() = mSecondaryProgress
        set(value) {
            if (mIndeterminate) return
            var secondaryProgressTemp = value
            if (secondaryProgressTemp < mMin) secondaryProgressTemp = mMin
            if (secondaryProgressTemp > mMax) secondaryProgressTemp = mMax
            if (secondaryProgressTemp != mSecondaryProgress) {
                mSecondaryProgress = secondaryProgressTemp
                postInvalidateOnAnimation()
            }
        }

    abstract fun AttributeSet.setupStyleable()
    abstract fun setupBackgroundProgress(mBackgroundRect: RectF, mBackgroundPaint: Paint)
    abstract fun setupProgress(mProgressRect: RectF, mProgressPaint: Paint)
    abstract fun setupSecondaryProgress(mSecondaryProgressRect: RectF, mSecondaryProgressPaint: Paint)
    abstract fun setupView()
    protected open fun onPreDrawBackground(mBackgroundRect: RectF, mBackgroundPaint: Paint) {}
    protected open fun onPreDrawProgress(mProgressRect: RectF, mProgressPaint: Paint, isReverse: Boolean) {}
    protected open fun onPreDrawSecondaryProgress(mSecondaryProgressRect: RectF, mSecondaryProgressPaint: Paint) {}

    init {
        setup(attrs)
    }

    private fun setup(attrs: AttributeSet?) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        attrs?.initStyleable()
        initView()
    }

    private fun AttributeSet.initStyleable() {
        val typedArray = mContext.obtainStyledAttributes(this, R.styleable.BaseRoundedCornerProgressBar)
        typedArray.let {
            animateDuration = it.getInt(R.styleable.BaseRoundedCornerProgressBar_rcpAnimateDuration, 1000).toLong()
            backgroundPadding = it.getDimensionPixelSize(R.styleable.BaseRoundedCornerProgressBar_rcpBackgroundPadding, 0).toFloat()
            mInterpolatorResId = it.getResourceId(R.styleable.BaseRoundedCornerProgressBar_rcpInterpolator, 0)
            indeterminate = it.getBoolean(R.styleable.BaseRoundedCornerProgressBar_rcpIndeterminate, false)
            isReverse = it.getBoolean(R.styleable.BaseRoundedCornerProgressBar_rcpIsReverse, false)
            max = it.getInt(R.styleable.BaseRoundedCornerProgressBar_rcpMax, MAX_PROGRESS)
            min = it.getInt(R.styleable.BaseRoundedCornerProgressBar_rcpMin, 0)
            progress = it.getInt(R.styleable.BaseRoundedCornerProgressBar_rcpProgress, 0)
            secondaryProgress = it.getInt(R.styleable.BaseSingleColorRoundedCornerProgressBar_rcpSecondaryProgressColor, 0)
            radius = it.getDimensionPixelSize(R.styleable.BaseRoundedCornerProgressBar_rcpRadius, 0).toFloat()
        }
        typedArray.recycle()
        setupStyleable()
    }

    protected fun getColor(@ColorRes colorResId: Int): Int {
        return if (colorResId != 0) {
            ContextCompat.getColor(mContext, colorResId)
        } else 0
    }

    private fun initView() {
        initInterpolator()
        initBackgroundProgress()
        initProgress()
        initSecondaryProgress()
        setupView()
    }

    private fun initInterpolator() {
        if (mInterpolatorResId != 0) setInterpolator(mInterpolatorResId)
    }

    private fun initBackgroundProgress() {
        mBackgroundRect = RectF()
        mBackgroundPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        mBackgroundRectWithPadding = RectF()
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                mBackgroundRect.set(0f, 0f,
                        this@BaseRoundedCornerProgressBar.width.toFloat(),
                        this@BaseRoundedCornerProgressBar.height.toFloat()
                )
                setupBackgroundProgress(mBackgroundRect, mBackgroundPaint)
            }
        })
    }

    private fun initProgress() {
        mProgressRect = RectF()
        mProgressPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        }
        setupProgress(mProgressRect, mProgressPaint)
    }

    private fun initSecondaryProgress() {
        mSecondaryProgressRect = RectF()
        mSecondaryProgressPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        }
        setupSecondaryProgress(mSecondaryProgressRect, mSecondaryProgressPaint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawAll()
    }

    private fun Canvas.drawAll() {
        drawBackground()
        drawSecondaryProgress()
        drawProgress()
    }

    protected fun Canvas.drawBackground() {
        onPreDrawBackground(mBackgroundRect, mBackgroundPaint)
        drawRoundRect(mBackgroundRect, mRadius, mRadius, mBackgroundPaint)
    }

    protected fun Canvas.drawSecondaryProgress() {
        if (mSecondaryProgress == 0) return
        val secondaryProgressPerMax = mSecondaryProgress.toProgressPerMax()
        if (!isReverse) setSecondaryProgressRect(secondaryProgressPerMax)
        else setSecondaryProgressRectReverse(secondaryProgressPerMax)
        onPreDrawSecondaryProgress(mSecondaryProgressRect, mSecondaryProgressPaint)
        drawRoundRect(mSecondaryProgressRect, mRadius, mRadius, mSecondaryProgressPaint)
    }

    private fun Canvas.setSecondaryProgressRect(secondaryProgressPerMax: Float) {
        mSecondaryProgressRect.set(
                -mRoundedWidth,
                0f,
                secondaryProgressPerMax,
                clipBounds.bottom.toFloat()
        )
    }

    private fun Canvas.setSecondaryProgressRectReverse(secondaryProgressPerMax: Float) {
        mSecondaryProgressRect.set(
                width - secondaryProgressPerMax,
                0f,
                width + mRoundedWidth,
                clipBounds.bottom.toFloat()
        )
    }

    protected fun Canvas.drawProgress() {
        val progressPerMax = mProgress.toProgressPerMax()
        if (!isReverse) setProgressRect(progressPerMax)
        else setProgressRectReverse(progressPerMax)
        onPreDrawProgress(mProgressRect, mProgressPaint, isReverse)
        drawRoundRect(mProgressRect, mRadius, mRadius, mProgressPaint)
    }

    private fun Canvas.setProgressRect(progressPerMax: Float) {
        mProgressRect.set(
                -mRoundedWidth,
                0f,
                progressPerMax,
                clipBounds.bottom.toFloat()
        )
    }

    private fun Canvas.setProgressRectReverse(progressPerMax: Float) {
        mProgressRect.set(
                width - progressPerMax,
                0f,
                width + mRoundedWidth,
                clipBounds.bottom.toFloat()
        )
    }

    protected fun Int.toProgressPerMax(): Float {
        val pureMax = mMax - mMin
        val pureProgress = this - mMin

        return ((mBackgroundRect.width() - (mBackgroundPadding * 2)) / pureMax) * pureProgress
    }

    fun setInterpolator(@InterpolatorRes interpolatorResId: Int) {
        this.mInterpolatorResId = interpolatorResId
        interpolator = AnimationUtils.loadInterpolator(mContext, interpolatorResId)
    }

    fun setProgressWithAnimate(progress: Int) {
        if (mIndeterminate) return
        var progressTemp = progress
        if (progressTemp == mProgress) return
        if (progressTemp < mMin) progressTemp = mMin
        if (progressTemp > mMax) progressTemp = mMax
        mProgress = progressTemp
        if (mProgress != mMin) {
            initProgressAnimator()
            startAnimation()
        }
    }

    private fun initProgressAnimator() {
        mProgressAnimator = ObjectAnimator.ofInt(this, "progress", mMin, mProgress).apply {
            interpolator = mInterpolator
            duration = mAnimateDuration
            mAnimatorListener?.let { addListener(it) }
            mAnimatorUpdateListener?.let { addUpdateListener(it) }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAnimatorPauseListener?.let { addPauseListener(it) }
            }
        }
    }

    private fun startAnimation(){
        mProgressAnimator?.start()
    }

    fun resumeAnimation() {
        mProgressAnimator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                it.resume()
            } else {
                it.start()
                it.currentPlayTime = mCurrentPlayTime
            }
        }
    }

    fun pauseAnimation() {
        mProgressAnimator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                it.pause()
            } else {
                mCurrentPlayTime = it.currentPlayTime
                cancelProgressAnimation()
            }
        }
    }

    fun getProgressAnimator() = mProgressAnimator

    private fun cancelProgressAnimation() {
        mProgressAnimator?.cancel()
    }

    fun addAnimatorListener(listener: Animator.AnimatorListener) {
        mAnimatorListener = listener
    }

    fun removeAnimatorListener(listener: Animator.AnimatorListener) {
        mProgressAnimator?.removeListener(listener)
    }

    fun removeAllAnimatorListener() {
        mProgressAnimator?.removeAllListeners()
    }

    fun addAnimatorUpdateListener(updateListener: ValueAnimator.AnimatorUpdateListener) {
        mAnimatorUpdateListener = updateListener
    }

    inline fun addAnimatorUpdateListener(
            crossinline onUpdate: (animation: ValueAnimator) -> Unit
    ): ValueAnimator.AnimatorUpdateListener {
        val listener = ValueAnimator.AnimatorUpdateListener {
            onUpdate(it)
        }
        addAnimatorUpdateListener(listener)
        return listener
    }

    fun removeAnimatorUpdateListener(updateListener: ValueAnimator.AnimatorUpdateListener) {
        mProgressAnimator?.removeUpdateListener(updateListener)
    }

    fun removeAllAnimatorUpdateListener() {
        mProgressAnimator?.removeAllUpdateListeners()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun addAnimatorPauseListener(pauseListener: Animator.AnimatorPauseListener) {
        mAnimatorPauseListener = pauseListener
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun removeAnimatorPauseListener(pauseListener: Animator.AnimatorPauseListener) {
        mProgressAnimator?.removePauseListener(pauseListener)
    }

    override fun onDetachedFromWindow() {
        cancelProgressAnimation()
        mProgressAnimator = null
        super.onDetachedFromWindow()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return superState?.let {
            val savedState = SavedState(it)
            savedState.mAnimateDuration = mAnimateDuration
            savedState.mIndeterminate = mIndeterminate
            savedState.mIsReverse = mIsReverse
            savedState.mMax = mMax
            savedState.mMin = mMin
            savedState.mRadius = mRadius
            savedState.mBackgroundPadding = mBackgroundPadding
            savedState.mProgress = mProgress
            savedState.mSecondaryProgress = mSecondaryProgress
            savedState.mCurrentPlayTime = mProgressAnimator?.currentPlayTime ?: 0
            savedState
        } ?: superState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state?.let {
            if (it is SavedState) {
                super.onRestoreInstanceState(it.superState)
                mAnimateDuration = it.mAnimateDuration
                mIndeterminate = it.mIndeterminate
                mIsReverse = it.mIsReverse
                mMax = it.mMax
                mMin = it.mMin
                mRadius = it.mRadius
                mBackgroundPadding = it.mBackgroundPadding
                mProgress = it.mProgress
                mSecondaryProgress = it.mSecondaryProgress
                mProgressAnimator?.currentPlayTime = it.mCurrentPlayTime
            } else super.onRestoreInstanceState(state)
        } ?: super.onRestoreInstanceState(state)
    }

    private class SavedState : BaseSavedState {
        var mAnimateDuration = PROGRESS_ANIM_DURATION
        var mIndeterminate: Boolean = false
        var mIsReverse: Boolean = false
        var mMax: Int = MAX_PROGRESS
        var mMin: Int = 0
        var mRadius: Float = 0f
        var mBackgroundPadding: Float = 0f
        var mProgress: Int = 0
        var mSecondaryProgress: Int = 0
        var mCurrentPlayTime: Long = 0

        constructor(source: Parcel?) : super(source) {
            source?.let {
                mAnimateDuration = it.readLong()
                mIndeterminate = it.readInt() != 0
                mIsReverse = it.readInt() != 0
                mMax = it.readInt()
                mMin = it.readInt()
                mRadius = it.readFloat()
                mBackgroundPadding = it.readFloat()
                mProgress = it.readInt()
                mSecondaryProgress = it.readInt()
                mCurrentPlayTime = it.readLong()
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        constructor(source: Parcel?, loader: ClassLoader?) : super(source, loader)
        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.let {
                it.writeLong(mAnimateDuration)
                it.writeInt(if (mIndeterminate) 1 else 0)
                it.writeInt(if (mIsReverse) 1 else 0)
                it.writeInt(mMax)
                it.writeInt(mMin)
                it.writeFloat(mRadius)
                it.writeFloat(mBackgroundPadding)
                it.writeInt(mProgress)
                it.writeInt(mSecondaryProgress)
                it.writeLong(mCurrentPlayTime)
            }
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(source: Parcel?) = SavedState(source)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }

    protected companion object {
        const val MAX_PROGRESS = 100
        const val PROGRESS_ANIM_DURATION = 1000L
    }
}