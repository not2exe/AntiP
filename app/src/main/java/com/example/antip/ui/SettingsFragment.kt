package com.example.antip.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.adapters.CustomAdapter
import com.example.antip.databinding.FragmentSettingsBinding
import com.example.antip.viewmodels.AppS
import com.example.antip.viewmodels.SettingsFragmentViewModel


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var bindingOrNull: FragmentSettingsBinding? = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety
    private val viewModel by viewModels<SettingsFragmentViewModel>()
    private val harmfulApps: ArrayList<AppS> = ArrayList<AppS>()
    private val usefulApps: ArrayList<AppS> = ArrayList<AppS>()
    private val otherApps: ArrayList<AppS> = ArrayList<AppS>()


    private fun initObservers() {
        viewModel.usefulApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> usefulApps.add(it1) }
            }

        }
        viewModel.harmfulApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> harmfulApps.add(it1) }
            }

        }
        viewModel.otherApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> otherApps.add(it1) }
            }

        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull = FragmentSettingsBinding.bind(view)
        val isInitShared =
            context?.getSharedPreferences("SettingsFragmentIsInit", Context.MODE_PRIVATE)?.all


        if (isInitShared?.get("Init") == false) {
            viewModel.initApps()
            val editor: SharedPreferences.Editor? =
                context?.getSharedPreferences("SettingsFragmentIsInit", Context.MODE_PRIVATE)
                    ?.edit()
            editor?.putBoolean("Init", true)
            editor?.apply()

        }
        initObservers()
        binding.rvHarmful.init(harmfulApps)
        binding.rvUseful.init(usefulApps)
        binding.rvOthers.init(otherApps)


    }


    private fun RecyclerView.init(list: ArrayList<AppS>) {
        val defaultG: AppS = AppS(context.getDrawable(R.drawable.undefined)!!, "gTime")
        this.layoutManager = LinearLayoutManager(context)

        val adapter = CustomAdapter(list, this@SettingsFragment, defaultG)
        this.adapter = adapter
        this.setOnDragListener(adapter.dragInstance)

    }


}