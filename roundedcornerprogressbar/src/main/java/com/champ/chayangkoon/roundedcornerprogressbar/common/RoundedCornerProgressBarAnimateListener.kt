package com.champ.chayangkoon.roundedcornerprogressbar.common

import android.animation.Animator
import android.os.Build
import androidx.annotation.RequiresApi

inline fun BaseRoundedCornerProgressBar.addAnimatorListener(
    crossinline onEnd: (animator: Animator) -> Unit = {},
    crossinline onStart: (animator: Animator) -> Unit = {},
    crossinline onCancel: (animator: Animator) -> Unit = {},
    crossinline onRepeat: (animator: Animator) -> Unit = {}
): Animator.AnimatorListener {
    val listener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animator: Animator) = onRepeat(animator)
        override fun onAnimationEnd(animator: Animator) = onEnd(animator)
        override fun onAnimationCancel(animator: Animator) = onCancel(animator)
        override fun onAnimationStart(animator: Animator) = onStart(animator)
    }
    addAnimatorListener(listener)
    return listener
}

@RequiresApi(Build.VERSION_CODES.KITKAT)
inline fun BaseRoundedCornerProgressBar.addAnimatorPauseListener(
    crossinline onResume: (animator: Animator) -> Unit = {},
    crossinline onPause: (animator: Animator) -> Unit = {}
): Animator.AnimatorPauseListener {
    val listener = object : Animator.AnimatorPauseListener {
        override fun onAnimationPause(animator: Animator) = onPause(animator)
        override fun onAnimationResume(animator: Animator) = onResume(animator)
    }
    addAnimatorPauseListener(listener)
    return listener
}

inline fun BaseRoundedCornerProgressBar.doOnEnd(crossinline action: (animator: Animator) -> Unit) =
    addAnimatorListener(onEnd = action)

inline fun BaseRoundedCornerProgressBar.doOnStart(crossinline action: (animator: Animator) -> Unit) =
    addAnimatorListener(onStart = action)


inline fun BaseRoundedCornerProgressBar.doOnCancel(crossinline action: (animator: Animator) -> Unit) =
    addAnimatorListener(onCancel = action)


inline fun BaseRoundedCornerProgressBar.doOnRepeat(crossinline action: (animator: Animator) -> Unit) =
    addAnimatorListener(onRepeat = action)

@RequiresApi(19)
inline fun BaseRoundedCornerProgressBar.doOnResume(crossinline action: (animator: Animator) -> Unit) =
    addAnimatorPauseListener(onResume = action)

@RequiresApi(19)
inline fun BaseRoundedCornerProgressBar.doOnPause(crossinline action: (animator: Animator) -> Unit) =
    addAnimatorPauseListener(onPause = action)