package com.example.antip


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.antip.service.DailyStatsService
import com.example.antip.service.Restarter


class MainActivity : AppCompatActivity() {
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fcvMainContainer) as NavHostFragment
    }

    private val navController by lazy { navHostFragment.findNavController() }

    private val requestCodeUsage = 101




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dailyStats = DailyStatsService()
        val dailyStatsIntent = Intent(this, dailyStats::class.java)
        if (!isServiceRunning(dailyStats::class.java))
            startService(dailyStatsIntent)
        //startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));





        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController,appBarConfiguration)




        }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.getRunningServices(Integer.MAX_VALUE).forEach {
            if (it.service.className == serviceClass.name) {
                return true
            }

        }
        return false

    }

    override fun onDestroy() {
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }




}


