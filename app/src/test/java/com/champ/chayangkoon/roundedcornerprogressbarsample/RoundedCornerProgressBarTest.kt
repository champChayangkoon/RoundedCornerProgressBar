package com.champ.chayangkoon.roundedcornerprogressbarsample

import android.graphics.Color
import android.os.Build
import android.os.Looper
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.champ.chayangkoon.roundedcornerprogressbar.RoundedCornerProgressBar
import com.champ.chayangkoon.roundedcornerprogressbarsample.utils.getColorCompat
import com.champ.chayangkoon.roundedcornerprogressbarsample.utils.getDimension
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class RoundedCornerProgressBarTest {
    private lateinit var activityController: ActivityController<AppCompatActivity>
    private lateinit var activity: AppCompatActivity
    private lateinit var roundedProgressBar: RoundedCornerProgressBar

    @Before
    fun setup() {
        activityController = Robolectric.buildActivity(AppCompatActivity::class.java)
        activity = activityController.setup().get()
        roundedProgressBar = RoundedCornerProgressBar(activity)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        activityController.pause().stop().destroy()
    }

    @Test
    fun testAssignAllAttribute() {
        val attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.rcpAnimateDuration, "3000")
                .addAttribute(R.attr.rcpIndeterminate, "false")
                .addAttribute(R.attr.rcpInterpolator, "@android:anim/accelerate_interpolator")
                .addAttribute(R.attr.rcpBackgroundPadding, "@dimen/background_padding")
                .addAttribute(R.attr.rcpIsReverse, "true")
                .addAttribute(R.attr.rcpMax, "100")
                .addAttribute(R.attr.rcpMin, "10")
                .addAttribute(R.attr.rcpProgress, "50")
                .addAttribute(R.attr.rcpRadius, "@dimen/radius")
                .addAttribute(R.attr.rcpBackgroundColor, "#000000")
                .addAttribute(R.attr.rcpProgressColor, "@color/colorAccent")
                .addAttribute(R.attr.rcpSecondaryProgressColor, "@color/colorPink")
                .build()

        roundedProgressBar = RoundedCornerProgressBar(activity, attributeSet)

        Truth.assertThat(roundedProgressBar.animateDuration).isEqualTo(3000L)
        Truth.assertThat(roundedProgressBar.indeterminate).isFalse()
        Truth.assertThat(roundedProgressBar.interpolator).isInstanceOf(LinearInterpolator::class.java)
        Truth.assertThat(roundedProgressBar.backgroundPadding).isEqualTo(activity.getDimension(R.dimen.background_padding))
        Truth.assertThat(roundedProgressBar.isReverse).isTrue()
        Truth.assertThat(roundedProgressBar.max).isEqualTo(100)
        Truth.assertThat(roundedProgressBar.min).isEqualTo(10)
        Truth.assertThat(roundedProgressBar.progress).isEqualTo(50)
        Truth.assertThat(roundedProgressBar.radius).isEqualTo(activity.getDimension(R.dimen.radius))
        Truth.assertThat(roundedProgressBar.backgroundProgressColor).isEqualTo(Color.parseColor("#000000"))
        Truth.assertThat(roundedProgressBar.progressColor).isEqualTo(activity.getColorCompat(R.color.colorAccent))
        Truth.assertThat(roundedProgressBar.secondaryProgressColor).isEqualTo(activity.getColorCompat(R.color.colorPink))
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    fun testSetProgressWithAnimate() {
        roundedProgressBar.apply {
            animateDuration = 2000
            max = 100
            min = 0
            backgroundProgressColor = Color.GRAY
            progressColor = activity.getColorCompat(R.color.colorAccent)
        }.also {
            it.setProgressWithAnimate(80)
        }

        Shadows.shadowOf(Looper.getMainLooper()).idle()

        Truth.assertThat(roundedProgressBar.max).isEqualTo(100)
        Truth.assertThat(roundedProgressBar.min).isEqualTo(0)
        Truth.assertThat(roundedProgressBar.getProgressAnimator()).isNotNull()
        Truth.assertThat(roundedProgressBar.progress).isEqualTo(80)
    }
}