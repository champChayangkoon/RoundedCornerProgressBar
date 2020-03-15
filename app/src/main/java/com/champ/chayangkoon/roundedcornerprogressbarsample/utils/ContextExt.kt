package com.champ.chayangkoon.roundedcornerprogressbarsample.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat


fun Context.getColorCompat(@ColorRes colorResIs : Int){
    ContextCompat.getColor(this,colorResIs)
}

fun Context.getDimension(@DimenRes dimenRes: Int) : Float{
    return resources.getDimension(dimenRes)
}
