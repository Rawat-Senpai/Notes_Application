package com.example.optimizingcode

import android.content.Context
import androidx.core.content.ContextCompat

class LightTheme: MyAppTheme{
    override fun backgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }

    override fun mainTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun changeIconColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun changeButtonColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun id(): Int {
        return  0
    }

}


class DarkTheme: MyAppTheme{
    override fun backgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun mainTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }

    override fun changeIconColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }


    override fun changeButtonColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun id(): Int {
        return  1
    }

}

