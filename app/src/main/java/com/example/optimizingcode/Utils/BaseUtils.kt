package com.example.optimizingcode.Utils

import android.content.Context
import android.content.SharedPreferences

object AppInfo {

    private var sharedPreferencesUser: SharedPreferences? = null
    private var editorUser: SharedPreferences.Editor? = null

    fun setContext(context: Context) {
        if (sharedPreferencesUser == null) {
            sharedPreferencesUser =
                context.getSharedPreferences("AppInfoUser", Context.MODE_PRIVATE);
            editorUser = sharedPreferencesUser!!.edit();
        }
    }


    fun setDarkMode(theme: Boolean) {
        editorUser?.putBoolean("theme", theme)
        editorUser?.commit()
    }

    fun getGetDarkMode(): Boolean {
        return sharedPreferencesUser?.getBoolean("theme", false)!!
    }




}