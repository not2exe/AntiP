package com.gtime


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.antip.R
import com.example.antip.databinding.ActivityMainBinding
import com.example.antip.databinding.NavHeaderMainBinding
import com.gtime.app.App
import com.gtime.model.IDRepository
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var idRepository: IDRepository

    @Inject
    lateinit var idIntent: Intent

    @Inject
    lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.fcvMainContainer)
        val navigationView = binding.navView
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainFragment
            ), drawerLayout
        )
        (applicationContext as App).appComponent.activity().create(this).inject(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        val headerBinding = NavHeaderMainBinding.bind(navigationView.getHeaderView(0))

        setObservers(headerBinding)
        setListeners(headerBinding)
    }

    private fun setObservers(binding: NavHeaderMainBinding) = with(binding) {
        idRepository.accountInfo.observe(this@MainActivity) {
            if (it.name == "") {
                authWithIDButton.visibility = View.VISIBLE
                nameTv.visibility = View.GONE
                emailTv.visibility = View.GONE
                iconIv.visibility = View.GONE
                loginOutButton.visibility = View.GONE
            } else {
                nameTv.text = it.name
                emailTv.text = it.email
                Glide.with(iconIv).load(it.urlAvatar).centerCrop().into(iconIv)
                authWithIDButton.visibility = View.GONE
                nameTv.visibility = View.VISIBLE
                emailTv.visibility = View.VISIBLE
                iconIv.visibility = View.VISIBLE
                loginOutButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setListeners(binding: NavHeaderMainBinding) {
        binding.authWithIDButton.setOnClickListener {
            launcher.launch(idIntent)
        }
        binding.loginOutButton.setOnClickListener {
            idRepository.clearAccountInfo()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fcvMainContainer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
