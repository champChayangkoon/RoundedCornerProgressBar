package com.champ.chayangkoon.roundedcornerprogressbarsample

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.champ.chayangkoon.roundedcornerprogressbar.common.BaseRoundedCornerProgressBar.Companion.MAX_PROGRESS
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roundedProgressBar.addAnimatorUpdateListener {
            progressBar.progress = it.animatedValue.toString().toFloat()
            roundedProgressBar.secondaryProgress = it.animatedValue.toString().toInt() + 100
            gradientRoundedProgressBar.progress = it.animatedValue.toString().toInt()
            Log.e("MainActivity", "MainActivity : ${it.animatedValue}")
        }

        gradientRoundedProgressBar.apply {
            max = 1000
            setBackgroundColors(R.color.colorCoin, R.color.colorCoin)
            setProgressColors(intArrayOf(
                    ContextCompat.getColor(this@MainActivity, R.color.colorLightPurple),
                    ContextCompat.getColor(this@MainActivity, R.color.colorPink),
                    ContextCompat.getColor(this@MainActivity, R.color.colorPrimary),
                    ContextCompat.getColor(this@MainActivity, R.color.colorGreen),
                    ContextCompat.getColor(this@MainActivity, R.color.colorYellow)
            ), floatArrayOf(
                    0f, .25f, .50f, .75f, 1f
            ))
        }

        roundedProgressBar.apply {
            animateDuration = 8000
            interpolator = DecelerateInterpolator()
            max = 1000
            setProgressWithAnimate(900)
        }


    }

    override fun onResume() {
        super.onResume()
        roundedProgressBar.resumeAnimation()
        gradientRoundedProgressBar.resumeAnimation()
    }

    override fun onPause() {
        roundedProgressBar.pauseAnimation()
        gradientRoundedProgressBar.pauseAnimation()
        super.onPause()
    }
}
