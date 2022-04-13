package com.example.antip


import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.app.usage.UsageStatsManager.INTERVAL_DAILY
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.databinding.ActivityMainBinding
import com.example.antip.db.GetUsageTime
import java.security.AccessController.getContext
import java.util.*


class MainActivity : AppCompatActivity() {
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fcvMainContainer) as NavHostFragment
    } // lazy initialization

    private val navController by lazy { navHostFragment.findNavController() }

    // private lateinit var navController: NavController late initializtion (don't forget to initialize this!)


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)



    }





}


