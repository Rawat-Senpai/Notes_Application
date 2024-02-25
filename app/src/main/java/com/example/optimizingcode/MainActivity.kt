package com.example.optimizingcode

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeActivity
import com.example.optimizingcode.Utils.AppInfo
import com.example.optimizingcode.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ThemeActivity() {


    private  lateinit var  binding: ActivityMainBinding
    private var fragmentNumber: Int = 0
    lateinit var  navController:NavController
    override fun getStartTheme(): AppTheme {

        AppInfo.setContext(this)
        return if(AppInfo.getGetDarkMode()){
            DarkTheme()
        }else {
            LightTheme()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController

//        setupActionBarWithNavController(navController)

        addNewFragment()

    }



    fun addNewFragment() {
        fragmentNumber++
        val transaction = supportFragmentManager.beginTransaction()

        if (fragmentNumber != 1) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }


    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
    }


}