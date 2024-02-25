package com.example.optimizingcode

import android.content.Context
import com.dolatkia.animatedThemeManager.AppTheme

interface MyAppTheme  :AppTheme {
    // for background for the screen
    fun backgroundColor(context: Context): Int

    // for text color main
    fun mainTextColor(context: Context): Int

    // for icon color icon color
    fun changeIconColor(context: Context): Int

    // fun for change button color

    fun changeButtonColor(context: Context):Int


}