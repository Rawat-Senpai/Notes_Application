package com.example.optimizingcode

import android.content.Context
import androidx.core.content.ContextCompat


object ColorObjects {

    private val colorResources = listOf(
        R.color.redColor,
        R.color.greenColor,
        R.color.blueColor,
        R.color.yellowColor,
        R.color.cyanColor,
        R.color.magentaColor
        // Add more color resources as needed
    )

    fun getRandomColor(context: Context): Int {
        val randomIndex = (0 until colorResources.size).random()
        return ContextCompat.getColor(context, colorResources[randomIndex])
    }


}


