package com.example.antip


import android.content.Intent
import android.content.pm.PackageItemInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController


class MainActivity : AppCompatActivity() {
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fcvMainContainer) as NavHostFragment
    } // lazy initialization

    private val navController by lazy { navHostFragment.findNavController() }

    private val requestCodeUsage = 101

    // private lateinit var navController: NavController late initializtion (don't forget to initialize this!)


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        val packageItemInfo=PackageItemInfo()

        Log.d("Checking",getInstalledComponentList().toString())


        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)



        }
    @Throws(PackageManager.NameNotFoundException::class)
    private fun getInstalledComponentList(): List<String>? {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val ril = packageManager.queryIntentActivities(mainIntent, 0)
        val componentList: MutableList<String> = ArrayList()
        var name: String? = null
        for (ri in ril) {
            if (ri.activityInfo != null) {
                val res: Resources =
                    packageManager.getResourcesForApplication(ri.activityInfo.applicationInfo)
                name = if (ri.activityInfo.labelRes != 0) {
                    res.getString(ri.activityInfo.labelRes)
                } else {
                    ri.activityInfo.applicationInfo.loadLabel(
                        packageManager
                    ).toString()
                }
                componentList.add(name)
            }
        }
        return componentList
    }



}


