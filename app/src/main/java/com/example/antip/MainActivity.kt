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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.databinding.ActivityMainBinding
import java.security.AccessController.getContext
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter=AppAdapter()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        init()

    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun init() = with(binding) {
        MainTable.layoutManager = LinearLayoutManager(this@MainActivity)
        MainTable.adapter = adapter
        var index = 0
        val lzero:Long=0
        val usageStatsManager:UsageStatsManager=getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val cal:Calendar=Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH,-1)
        val queryUsageStats:List<UsageStats> = usageStatsManager.queryUsageStats(INTERVAL_DAILY,
            cal.timeInMillis,
            System.currentTimeMillis()
        )
        queryUsageStats.sortedBy { it.totalTimeVisible }
        var statsData=""
        for (i in 0..queryUsageStats.size-1){
            if (queryUsageStats[i].totalTimeVisible != lzero)
            {


                val app = App(
                    getIconApp(queryUsageStats[i].packageName),
                    queryUsageStats[i].packageName,
                    (queryUsageStats[i].totalTimeVisible).toString()
                )
                adapter.addApp(app)
                index++

            }
        }



    }
    private fun getIconApp(packageName:String):Drawable{
        try {
            val icon: Drawable = packageManager.getApplicationIcon(packageName)
            return icon
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.d("NameNotFoundException","yes")

        }
        return getDrawable(R.drawable.app1)!!

    }



}


