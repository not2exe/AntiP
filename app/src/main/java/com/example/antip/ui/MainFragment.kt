package com.example.antip.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.App
import com.example.antip.AppAdapter
import com.example.antip.R
import com.example.antip.databinding.FragmentMainBinding
import com.example.antip.viewmodels.MainFragmentViewModel
import com.example.antip.viewmodels.State


class MainFragment : Fragment(R.layout.fragment_main) {
    private var bindingOrNull: FragmentMainBinding?  = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety
    private val usefulAdapter=AppAdapter()
    private val harmfulAdapter=AppAdapter()
    private var state:State=State.USEFUL
    private var otherApps:ArrayList<App> = ArrayList<App>()

    var cash:SharedPreferences?=null

    val viewModel by viewModels<MainFragmentViewModel>() // View model initialization with delegate property



    private fun initObservers()= with(binding){
        MainTable.adapter=usefulAdapter
        MainTable.layoutManager=LinearLayoutManager(context)

        viewModel.usefulApps.observe(viewLifecycleOwner){
            for (i in it.indices){
                it[i]?.let { it1 -> usefulAdapter.addApp(it1) }
            }

        }
        viewModel.harmfulApps.observe(viewLifecycleOwner){
            for (i in it.indices){
                it[i]?.let { it1 -> harmfulAdapter.addApp(it1) }
            }

        }
        viewModel.otherApps.observe(viewLifecycleOwner){
            for (i in it.indices){
                it[i]?.let { it1 -> otherApps.add(it1) }
            }

        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull = FragmentMainBinding.bind(view)
        viewModel.initApps()
        viewModel.gang()

        binding.buttonChangeAdapter.setOnClickListener {
            onClickChangeButton()
        }
        binding.buttonSettings.setOnClickListener {
            onClickSettingsButton()
        }
        initObservers()


    }
    
    private fun onClickChangeButton(){
        when (state){
            State.USEFUL-> {
                binding.MainTable.adapter=harmfulAdapter
                state=State.USELESS
            }
            State.USELESS->{
                binding.MainTable.adapter=usefulAdapter
                state=State.USEFUL
            }
        }
        
    }
    private fun onClickSettingsButton(){
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())


    }



}